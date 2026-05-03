FROM node:24-alpine AS build

WORKDIR /app

COPY frontend/med-manager-front/package*.json ./
RUN npm install

COPY frontend/med-manager-front/ .
RUN npm run build

FROM nginx:alpine

COPY --from=build /app/dist /usr/share/nginx/html
COPY deploy/nginx/nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
