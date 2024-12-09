# Etapa 1: Compilar la aplicación Angular 18
FROM node:21-alpine AS builder 

# Instalar Angular CLI globalmente
 RUN npm install -g @angular/cli@18.0.0 

WORKDIR /app 

# Copiar los archivos del paquete para instalar las dependencias
 COPY package*.json ./ 

# Instalar las dependencias
 RUN npm install 

# Copiar el resto de los archivos de la aplicación
 COPY . . 

# Compilar la aplicación Angular para producción
 RUN ng build --configuration=production 

# Etapa 2: Configurar el backend de Node.js 21
 FROM node:21-alpine AS server 

WORKDIR /app 

# Copiar solo los archivos del paquete para el backend de Node.js
 COPY src/installer/package*.json ./ 

# Instalar las dependencias del servidor
 RUN npm install 

# Copiar los archivos fuente del backend
 COPY src/installer . 

# Exponga el puerto en el que se ejecutará su servidor Node.js
 EXPOSE 3000 

# Comando para ejecutar el servidor Node.js
 CMD [ "node" , "index.js" ] 

# Etapa 3: Combine Angular y Node.js con NGINX
 FROM nginx:alpine 

# Copie la compilación de Angular desde la etapa de compilación a NGINX
 COPY --from=builder /app/dist/<your-angular-project-name> /usr/share/nginx/html 

# Copie los archivos del servidor Node.js desde la etapa del servidor
 COPY --from=server /app /usr/share/nginx/node-server 

# Instale Node.js en la etapa final para soportar operaciones de backend
 RUN apk add --no-cache nodejs npm 

# Exponga los puertos necesarios
 EXPOSE 80   # NGINX
 EXPOSE 3000   # API de Node.js 

# Inicie NGINX y el servidor Node.js simultáneamente
 CMD nginx -g "daemon off;" & node /usr/share/nginx/node-server/index.js