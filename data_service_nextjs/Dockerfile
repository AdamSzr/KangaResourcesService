FROM node:16-alpine
WORKDIR /data_service_nextjs
COPY . .

ENV PWD="/data_service_nextjs"

RUN npm install 
RUN npm run build

CMD npm run start