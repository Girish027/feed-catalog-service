package feed.catalog.api.response.utils.validator.annotations.implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import feed.catalog.api.response.handler.FeedCatalogInvocableException;
import feed.catalog.api.response.handler.HandlerResponse;
import feed.catalog.api.response.utils.MessageHandler;
import feed.catalog.api.response.utils.validator.annotations.DTO;
import feed.catalog.api.response.utils.validator.annotations.interfaces.DTOSymentic;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.*;

public class DTOModelMapperImpl extends RequestResponseBodyMethodProcessor {

    private static final ModelMapper modelMapper = new ModelMapper();

    private static final Map<String,Class> activeUDTProps = new HashMap<>();

    private static final Gson gson = new GsonBuilder().setLenient().setPrettyPrinting().create();

    public DTOModelMapperImpl(ObjectMapper objectMapper,List<Object> DTOBeans) {
        super(Collections.singletonList(new MappingJackson2HttpMessageConverter(objectMapper)));
        bootStrapingUDTs(DTOBeans);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(DTO.class) && parameter.hasParameterAnnotation(RequestBody.class);
    }

    @Override
    protected void validateIfApplicable(WebDataBinder binder, MethodParameter parameter) {
        binder.validate();
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Object dto = super.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
        if (null!=dto)
            return modelMapper.map(dto, parameter.getParameterType());
        throw new MethodArgumentNotValidException(parameter, new BeanPropertyBindingResult(dto,binderFactory.toString()));
    }

    @Override
    protected Object readWithMessageConverters(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType) throws IOException, HttpMediaTypeNotSupportedException, HttpMessageNotReadableException {
        for (Annotation ann : parameter.getParameterAnnotations()) {
            DTO dtoType = AnnotationUtils.getAnnotation(ann, DTO.class);
            if (dtoType != null) {
                return super.readWithMessageConverters(inputMessage, parameter, dtoType.value());
            }
        }
        throw new RuntimeException();
    }

    private static MessageHandler propertiesDispatcher(Object src, BeanWrapper srcWrap ) throws ClassNotFoundException {
        final Set<String> activePrimitiProps = new HashSet<>();

        Arrays.stream(BeanUtils.getPropertyDescriptors(src.getClass())).parallel().forEach(pd ->{
            Object srcValue = srcWrap.getPropertyValue(pd.getName());
            if (srcValue != null && !pd.getName().equalsIgnoreCase("class") && !activeUDTProps.containsKey(pd.getName()))
                if(DTOSymentic.class.isAssignableFrom(pd.getReadMethod().getReturnType())) {
                    activeUDTProps.put(pd.getName(),pd.getReadMethod().getReturnType());
                }
                else
                    activePrimitiProps.add(pd.getName());
        });

        return new MessageHandler(activePrimitiProps,activeUDTProps);
    }

    private void bootStrapingUDTs(List<Object> DTOBeans){
        for(Object dto : DTOBeans) {
            Arrays.stream(BeanUtils.getPropertyDescriptors(dto.getClass())).parallel().forEach(pd -> {
                if (!pd.getName().equalsIgnoreCase("class")
                        && !activeUDTProps.containsKey(pd.getName())
                        &&
                        (DTOSymentic.class.isAssignableFrom(pd.getReadMethod().getReturnType()) || Collection.class.isAssignableFrom(pd.getReadMethod().getReturnType())))
                    activeUDTProps.put(pd.getName(), pd.getReadMethod().getReturnType());
            });
        }
    }


    public static <T,S> T mapper(S src, T trg) {
        try {
            final BeanWrapper srcWrap = PropertyAccessorFactory.forBeanPropertyAccess(src);
            final BeanWrapper trgWrap = PropertyAccessorFactory.forBeanPropertyAccess(trg);
            MessageHandler props = propertiesDispatcher(src, srcWrap);
            if (trg.getClass().isAnnotationPresent(DTO.class)){
                props.getUdt().keySet().forEach(p -> {
                    if(null != trgWrap.getPropertyType(p))
                        if(Collection.class.isAssignableFrom(trgWrap.getPropertyType(p))){
                            Type type = TypeToken.getParameterized(trgWrap.getPropertyTypeDescriptor(p).getType(), trgWrap.getPropertyTypeDescriptor(p).getElementTypeDescriptor().getType()).getType();
                            trgWrap.setPropertyValue(p, gson.fromJson(srcWrap.getPropertyValue(p).toString().substring(1,srcWrap.getPropertyValue(p).toString().length()-1), type));
                        }
                        else
                            trgWrap.setPropertyValue(p, gson.fromJson(srcWrap.getPropertyValue(p).toString(), trgWrap.getPropertyType(p)));
                });
                props.getPrimitive().forEach(p -> {
                    if(null != trgWrap.getPropertyType(p))
                        trgWrap.setPropertyValue(p, srcWrap.getPropertyValue(p));});
            }
            else {
                props.getPrimitive().forEach(p -> trgWrap.setPropertyValue(p, srcWrap.getPropertyValue(p)));
                props.getUdt().keySet().forEach(p -> {
                    if(null!=srcWrap.getPropertyType(p) && null!=srcWrap.getPropertyValue(p))
                        trgWrap.setPropertyValue(p, gson.toJson(srcWrap.getPropertyValue(p)));
                });
            }
            return trg;
        }
        catch (Exception ex){
            throw new FeedCatalogInvocableException(
                    HandlerResponse.builder().httpStatus(HttpStatus.INTERNAL_SERVER_ERROR).details(Arrays.asList("Input format incorrect")).message(ex.getMessage()).build());
        }
    }

    public static <T,S> T mapperDTO(S src, T trg) {
        try {
            final BeanWrapper srcWrap = PropertyAccessorFactory.forBeanPropertyAccess(src);
            final BeanWrapper trgWrap = PropertyAccessorFactory.forBeanPropertyAccess(trg);
            MessageHandler props = propertiesDispatcher(src, srcWrap);
            props.getPrimitive().forEach(p -> trgWrap.setPropertyValue(p, srcWrap.getPropertyValue(p.toString())));
            props.getUdt().keySet().forEach(p -> {
                if(null!=srcWrap.getPropertyType(p) && null!=srcWrap.getPropertyValue(p))
                    trgWrap.setPropertyValue(p, srcWrap.getPropertyValue(p));
            });            return trg;
            }
        catch (Exception ex){
            throw new FeedCatalogInvocableException(
                    HandlerResponse.builder().httpStatus(HttpStatus.INTERNAL_SERVER_ERROR).details(Arrays.asList("Input format incorrect")).message(ex.getMessage()).build());
        }
    }
}