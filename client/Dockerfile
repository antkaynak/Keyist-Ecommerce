FROM node:12.7-alpine AS build

WORKDIR /usr/src/app

COPY package.json package-lock.json ./
RUN npm install

COPY . .
RUN npm run build

FROM nginx:1.17.1-alpine

COPY .nginx.conf /etc/nginx/nginx.conf

RUN rm -rf /usr/share/nginx/html/*

COPY --from=build /usr/src/app/dist/client /usr/share/nginx/html

EXPOSE 4200 80
ENTRYPOINT ["nginx", "-g", "daemon off;"]
