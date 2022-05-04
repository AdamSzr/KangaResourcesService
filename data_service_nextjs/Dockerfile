FROM node:16-alpine
WORKDIR /data_service_nextjs
COPY . .

RUN npm install

RUN npm run build

CMD npm run start