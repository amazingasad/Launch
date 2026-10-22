@echo off
setlocal
set APP_HOME=%~dp0
if not defined JAVA_HOME goto findJavaFromPath
set JAVA_EXE=%JAVA_HOME%\bin\java.exe
if exist "%JAVA_EXE%" goto execute
:findJavaFromPath
set JAVA_EXE=java.exe
:execute
"%JAVA_EXE%" -Dorg.gradle.appname=gradlew -classpath "%APP_HOME%gradle\wrapper\gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain %*
endlocal
