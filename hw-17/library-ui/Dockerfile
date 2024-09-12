#FROM node:16
#
#WORKDIR /app
#
#COPY package.json package-lock.json ./
#
#RUN npm install
#
#COPY . .
#
#CMD ["npm", "run", "dev"]

# Use a specific version of the Node.js image
#FROM node:16
#
## Set a working directory
#WORKDIR /app
#
## Copy only package.json and package-lock.json
#COPY package.json package-lock.json ./
#
## Install dependencies
#RUN npm ci
#
## Copy the rest of the application code
#COPY . .
##
## Optionally, you might want to run a build step here if needed
#RUN npm run build
#
## Run the application
#CMD ["npm", "run", "dev"]


FROM node:16 AS build

WORKDIR /app

COPY package.json package-lock.json ./
RUN npm install

COPY . .

# Build the static files for production
RUN npm run build

# Production Stage
FROM nginx:alpine

# Copy built static files from the build stage
COPY --from=build /app/build /usr/share/nginx/html
COPY default.conf /etc/nginx/conf.d/default.conf

# Expose port 80 for NGINX
EXPOSE 80

