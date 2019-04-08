FROM node:11-alpine as react-build
EXPOSE 3000
WORKDIR /app
COPY . ./
RUN npm install
CMD ["npm", "start"]