taskkill /IM "rcssserver.exe" /F
taskkill /IM "soccerwindow2.exe" /F
cd rcssserver-14.0.3-win
start rcssserver
cd ..
rem timeout /t 1 /nobreak
cd soccerwindow2-5.0.0-win
start SoccerWindow2
cd ..
java -jar BrunoTeamRC2D-deploy.jar
