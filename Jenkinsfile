buildSuccess = true
Master_Build_ID = "1.1.0-${env.BRANCH_NAME}-${env.BUILD_NUMBER}"
grpId = "com.tfs.dp"
artId = "Feed-Service"
packageType = "jar"
artifactName = "${artId}-${Master_Build_ID}.${packageType}"
archiveLocation = "./build/libs/dp2-FEED_SERVICE_sanity-0.0.1-SNAPSHOT.jar"
workspace = env.WORKSPACE

mavenVersion="apache-maven-3.3.3"
nodejsVersion="node-v4.4.6-linux-x64"
grailsVersion="grails-2.5.0"
gradleVersion="gradle-2.3"

node
{
	env.JAVA_HOME = "${env.jdk7_home}"
	sh "${env.JAVA_HOME}/bin/java -version"
	echo "Current branch <${env.BRANCH_NAME}>"
	def workspace = env.WORKSPACE
	
	stage('Preparation') 
	{
		executeCheckout()
		pom = readMavenPom file: 'pom.xml'
	}
	if(env.CHANGE_ID)
	{
		stage('commit')
		{
			echo "pull request detected"
			buildSuccess = executeBuild()
			echo "buildSuccess = ${buildSuccess}"
			validateBuild(buildSuccess)
		}
		
	}
	if(!env.CHANGE_ID)
	{
		stage('sanity')
		{
			echo "push detected"
			buildSuccess = executeBuildsanity()
			echo "buildSuccess = ${buildSuccess}"
			validateBuild(buildSuccess)
		}		
	}
}
def boolean executeBuild()
{
	def result = true
	def branchName = env.BRANCH_NAME
	echo "branch = ${branchName} Master_Build_ID=${Master_Build_ID}"
			try 
			{
				sh '''	export JAVA_HOME=${jdk8_home}
						export PATH=${jdk8_home}/bin:$PATH
						mavenVersion='''+mavenVersion+'''
						nodejsVersion='''+nodejsVersion+'''
						grailsVersion='''+grailsVersion+'''
						gradleVersion='''+gradleVersion+'''
						
						export PATH=$PATH:/opt/${mavenVersion}/bin
						export PATH=/opt/${nodejsVersion}/bin:$PATH
						export PATH=/var/tellme/jenkins/tools/sbt/bin:$PATH
						export PATH=/opt/${grailsVersion}/bin:$PATH
						export PATH=/opt/${gradleVersion}/bin:$PATH
						
						BRANCH='''+branchName+'''
					#ADD YOUR BUILD STEPS HERE----------------------------------
						export http_proxy=http://proxy-grp1.lb-priv.sv2.247-inc.net:3128
						export https_proxy=http://proxy-grp1.lb-priv.sv2.247-inc.net:3128
						/opt/${mavenVersion}/bin/mvn -Dhttp.proxyHost=proxy-grp1.lb-priv.sv2.247-inc.net -Dhttp.proxyPort=3128 -Dhttps.proxyHost=proxy-grp1.lb-priv.sv2.247-inc.net -Dhttps.proxyPort=3128 clean install
					#-----------------------------------------------------------
				'''
				echo "Build Success...."
				result = true
			} 
			catch(Exception ex) 
			{
				 echo "Build Failed...."
				 echo "ex.toString() - ${ex.toString()}"
				 echo "ex.getMessage() - ${ex.getMessage()}"
				 echo "ex.getStackTrace() - ${ex.getStackTrace()}"
				 result = false
			} 
		
	
	echo "result - ${result}"
	result
}
def executeBuildsanity()
{
	def result = true
	def branchName = env.BRANCH_NAME
	echo "branch = ${branchName}"
	try 
	{
		sh '''	export JAVA_HOME=${jdk8_home}
				export PATH=${jdk8_home}/bin:$PATH
				mavenVersion='''+mavenVersion+'''
				nodejsVersion='''+nodejsVersion+'''
				grailsVersion='''+grailsVersion+'''
				gradleVersion='''+gradleVersion+'''
				
				export PATH=$PATH:/opt/${mavenVersion}/bin
				export PATH=/opt/${nodejsVersion}/bin:$PATH
				export PATH=/var/tellme/jenkins/tools/sbt/bin:$PATH
				export PATH=/opt/${grailsVersion}/bin:$PATH
				export PATH=/opt/${gradleVersion}/bin:$PATH
				
				BRANCH='''+branchName+'''
				#ADD YOUR BUILD STEPS HERE----------------------------------
				export http_proxy=http://proxy-grp1.lb-priv.sv2.247-inc.net:3128
				export https_proxy=http://proxy-grp1.lb-priv.sv2.247-inc.net:3128
				/opt/${mavenVersion}/bin/mvn -Dhttp.proxyHost=proxy-grp1.lb-priv.sv2.247-inc.net -Dhttp.proxyPort=3128 -Dhttps.proxyHost=proxy-grp1.lb-priv.sv2.247-inc.net -Dhttps.proxyPort=3128 clean install -Dnexus.url="${NEXUS_REPO_URL_DEFAULT}" -Dnexus.repo.id="${NEXUS_REPO_ID_DEFAULT}"
				
			
		'''
		Master_Build_ID=sh returnStdout: true, script: '''ART_ID='''+artId+'''
			mavenVersion='''+mavenVersion+'''
			PACKAGE_TYPE='''+packageType+'''
			WS_LOCATION=$(pwd)
			cd ${WS_LOCATION}/target
			ZIP_FILE=$(ls $ART_ID-*.$PACKAGE_TYPE)
			REPO_URL=${NEXUS_REPO_URL_DEFAULT}
			REPO_ID=${NEXUS_REPO_ID_DEFAULT}
			GRP_ID='''+grpId+'''
			ART_ID='''+artId+'''
			Master_Build_ID='''+Master_Build_ID+'''
			/opt/${mavenVersion}/bin/mvn -B deploy:deploy-file -Durl=$REPO_URL -DrepositoryId=$REPO_ID -DgroupId=$GRP_ID -DartifactId=$ART_ID -Dversion=$Master_Build_ID -Dfile=$ZIP_FILE -Dpackaging=$PACKAGE_TYPE -DgeneratePom=true -e
		   	/opt/${mavenVersion}/bin/mvn -B deploy:deploy-file -Durl=$REPO_URL -DrepositoryId=$REPO_ID -DgroupId=$GRP_ID -DartifactId=$ART_ID -Dversion=latest -Dfile=$ZIP_FILE -Dpackaging=$PACKAGE_TYPE -DgeneratePom=true -e
			echo $Master_Build_ID'''
		Master_Build_ID=Master_Build_ID.replaceAll("\n", "")
		artifactName = "${artId}-${Master_Build_ID}.${packageType}"
		archiveLocation = "./target/${artifactName}"
		echo "master build id = ${Master_Build_ID}, artifactName = ${artifactName}"
		
		echo "Build Success...."
		result = true
	}
	catch(Exception ex) 
	{
		 echo "Build Failed...."
		 echo "ex.toString() - ${ex.toString()}"
		 echo "ex.getMessage() - ${ex.getMessage()}"
		 echo "ex.getStackTrace() - ${ex.getStackTrace()}"
		 result = false
	} 
	result 
}
def executeCheckout()
{
  //Get some code from a GitHub repository
  checkout scm
}

def getBuildPromotionApproval(def toStage)
{
	def userInput= ""
	try{
		userInput = input (message: "Promote to ${toStage} environment", parameters: [choice(choices: ['YES ', 'SKIP'], description: "Choices to promote the build to ${toStage} environment", name: 'PROMOTION')])
		echo ("proceed promotion = " + userInput)
	}
	catch(Exception ex)
	{
		echo "Aborting this build! no more promotions can be made! "
		try
		{
			input (message: 'Proceed abortion of the build?')
			error "build aborted!"
		}
		catch(Exception e){
			echo "abort cancelled!"
		}
	
	}
	userInput
}

def validateBuild(def buildStatus)
{
	if (buildStatus) 
	{
		  currentBuild.result = 'SUCCESS'
	}
	else
	{
		currentBuild.result = 'FAILURE'
		 error "build failed!"
	}
	
}
def promote(def promoteTo, def artifactName, def grpId, def artId, def artVersion, def packageType)
{
	sh 	'''	mavenVersion='''+mavenVersion+'''
			export _JAVA_OPTIONS=-Djava.io.tmpdir=/var/tellme/jenkins/tmp
			WS_LOCATION=$(pwd)
			export JAVA_HOME=${jdk7_home}
			ARTIFACTS_FILE='''+artifactName+'''
			GRP_ID='''+grpId+'''
			GRP_ID_with_slash=$(echo $GRP_ID | sed "s/\\./\\//g")
			ART_ID='''+artId+'''
			PACKAGE_TYPE='''+packageType+'''
			promote_to='''+promoteTo+'''
			ARTIFACTS_VERSION='''+artVersion+'''
			if [ "$promote_to" = "qa" ]; then
				ARTIFACT_URL=${NEXUS_REPO_URL_DEFAULT}/$GRP_ID_with_slash/$ART_ID/${ARTIFACTS_VERSION}/${ARTIFACTS_FILE}
				REPO_URL=${NEXUS_REPO_URL_QA}
				REPO_ID=${NEXUS_REPO_ID_QA}
				REPO_PATH="promoted"
					
			elif [ "$promote_to" = "stable" ]; then
				ARTIFACT_URL=${NEXUS_REPO_URL_QA}/$GRP_ID_with_slash/$ART_ID/${ARTIFACTS_VERSION}/${ARTIFACTS_FILE}
				REPO_URL=${NEXUS_REPO_URL_STABLE}
				REPO_ID=${NEXUS_REPO_ID_STABLE}
				REPO_PATH="promoted"
			elif [ "$promote_to" = "pre-prod-stage" ]; then
				ARTIFACT_URL=${NEXUS_REPO_URL_STABLE}/$GRP_ID_with_slash/$ART_ID/${ARTIFACTS_VERSION}/${ARTIFACTS_FILE}
				REPO_URL=${NEXUS_REPO_URL_PRE_PROD_STAGE}
				REPO_ID=${NEXUS_REPO_ID_PRE_PROD_STAGE}
				REPO_PATH="promoted"
			elif [ "$promote_to" = "production" ]; then
				ARTIFACT_URL=${NEXUS_REPO_URL_PRE_PROD_STAGE}/$GRP_ID_with_slash/$ART_ID/${ARTIFACTS_VERSION}/${ARTIFACTS_FILE}
				REPO_URL=${NEXUS_REPO_URL_PRODUCTION}
				REPO_ID=${NEXUS_REPO_ID_PRODUCTION}
				REPO_PATH="promoted"
			fi
			
			wget $ARTIFACT_URL --proxy=off > /dev/null 2>&1
			echo "#############################################################################################"
			#echo "Update build $PACKAGE_TYPE and uploading to nexus..."
			WS_LOCATION=$(pwd)
			#echo "create $PACKAGE_TYPE file"
			ZIP_FILE=$WS_LOCATION/${ARTIFACTS_FILE}
			/opt/${mavenVersion}/bin/mvn deploy:deploy-file -Durl=$REPO_URL -DrepositoryId=$REPO_ID -DgroupId=$GRP_ID -DartifactId=$ART_ID -Dversion=$REPO_PATH -Dpackaging=$PACKAGE_TYPE -Dfile=$ZIP_FILE -DgeneratePom=true -e > /dev/null 2>&1
			/opt/${mavenVersion}/bin/mvn deploy:deploy-file -Durl=$REPO_URL -DrepositoryId=$REPO_ID -DgroupId=$GRP_ID -DartifactId=$ART_ID -Dversion=$ARTIFACTS_VERSION -Dpackaging=$PACKAGE_TYPE -Dfile=$ZIP_FILE -DgeneratePom=true -e > /dev/null 2>&1
			'''
}
