@REM docker stop data_service_nextjs & docker rmi data_service_nextjs &&
docker rmi data_service_nextjs && docker build -t data_service_nextjs . && docker run -d -p 3000:3000 data_service_nextjs