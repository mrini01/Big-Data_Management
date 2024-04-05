@echo off
setlocal enabledelayedexpansion

rem Define the version for the beast-spark package
set "beast_version=0.10.1-RC1"

rem Get the directory where the script is located
set "script_dir=%~dp0"

rem Check if "lib" directory exists in the script directory, create it if not
if not exist "%script_dir%lib\" (
    mkdir "%script_dir%lib"
)

rem Check if "jai_core-1.1.3.jar" exists in "lib" directory, download if not
if not exist "%script_dir%lib\jai_core-1.1.3.jar" (
    echo JAR file not found. Downloading...
    curl -o "%script_dir%lib\jai_core-1.1.3.jar" https://repo.osgeo.org/repository/geotools-releases/javax/media/jai_core/1.1.3/jai_core-1.1.3.jar
)

set "spark_jars="

for %%i in ("%script_dir%lib\*.jar") do (
    if "!spark_jars!" == "" (
        set "spark_jars=%%i"
    ) else (
        set "spark_jars=!spark_jars!,%%i"
    )
)

rem Initialize default packages and repositories
set "spark_packages=org.mortbay.jetty:jetty:6.1.26,org.eclipse.jetty.jetty-servlet:9.4.48.v20220622,org.eclipse.jetty:jetty-server:9.4.48.v20220622,org.geotools:gt-epsg-hsql:23.0,org.geotools:gt-coverage:23.0,org.locationtech.jts:jts-core:1.17.1"

rem Check if "beast-spark-*.jar" exists in "lib" directory, add package if not
if not exist "%script_dir%lib\beast-spark-*.jar" (
    echo Local Beast libraries not found. Using the latest version from Maven Central
    set "spark_packages=edu.ucr.cs.bdlab:beast-spark:%beast_version%,%spark_packages%"
)

set "spark_repositories=https://repo.osgeo.org/repository/release/"
set "spark_excluded_packages=org.apache.hadoop:hadoop-common,org.apache.hadoop:hadoop-hdfs,javax.media:jai_core"
set "spark_args="
set "additional_args=."

rem Process special arguments --jars, --packages, and --repositories
:parse_args
if "%~1" == "" goto run_spark_submit
if "!additional_args!" == "." (
  if "%~1" == "--jars" (
      if "!spark_jars!" == "" (
          set "spark_jars=%~2"
      ) else (
          set "spark_jars=!spark_jars!,%~2"
      )
      shift
      shift
      goto parse_args
  )
  if "%~1" == "--packages" (
      set "spark_packages=!spark_packages!,%~2"
      shift
      shift
      goto parse_args
  )
  if "%~1" == "--excluded-packages" (
      set "spark_excluded_packages=!spark_excluded_packages!,%~2"
      shift
      shift
      goto parse_args
  )
  if "%~1" == "--repositories" (
      set "spark_repositories=!spark_repositories!,%~2"
      shift
      shift
      goto parse_args
  )
  set "argument=%~1"
  if "!argument:~0,2!"=="--" (
      set "spark_args=!spark_args! %~1 %~2"
      shift
      shift
      goto parse_args
  )
)
set "argument=%~1"
if "!additional_args!"=="." (
    rem First argument. Check if it points to a valid file
    if exist "%argument%" (
        rem Remove the "." from the additional args
        set "additional_args="
    )
)
set "additional_args=!additional_args! !argument!"
shift
goto parse_args

:run_spark_submit
rem Construct the spark-submit command with all arguments
set "spark_submit_command=spark-submit"
set "spark_submit_arguments=--jars !spark_jars!"
set "spark_submit_arguments=!spark_submit_arguments! --packages !spark_packages!"
set "spark_submit_arguments=!spark_submit_arguments! --excluded_packages !spark_excluded_packages!"
set "spark_submit_arguments=!spark_submit_arguments! --repositories !spark_repositories!"
set "spark_submit_arguments=!spark_submit_arguments! !spark_args!"

echo Running the following command:
echo !spark_submit_command! !spark_submit_arguments! !additional_args!
echo.

rem Run the spark-submit command
!spark_submit_command! !spark_submit_arguments!

endlocal
