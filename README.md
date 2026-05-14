# File Manager Service - Lab 07

DO Spaces ашиглан файл хадгалдаг Spring Boot сервис.

## Ажиллуулах

### 1. Environment variable тохируулах
```bash
cp .env.example .env
# .env файлд бодит утгуудаа бичнэ
```

### 2. Локал дээр ажиллуулах
```bash
# S3 тохиргоо тавина
export S3_ACCESS_KEY=your_key
export S3_SECRET_KEY=your_secret
export S3_BUCKET_NAME=your-bucket
export S3_REGION=sgp1
export SOAP_SERVICE_URL=http://localhost:8081/UserService

mvn spring-boot:run
```

### 3. Docker ашиглан ажиллуулах
```bash
docker build -t file-manager-service .
docker run -p 8080:8080 \
  -e S3_ACCESS_KEY=xxx \
  -e S3_SECRET_KEY=yyy \
  -e S3_BUCKET_NAME=my-bucket \
  -e S3_REGION=sgp1 \
  -e SOAP_SERVICE_URL=http://soap-ip:8080/UserService \
  file-manager-service
```

## API

### POST /files/upload
Файл хуулах. SOAP токен шаардлагатай.

**Headers:**
- `Authorization: Bearer <token>`
- `Content-Type: multipart/form-data`

**Body:**
- `file`: хуулах файл

**Хариу (200 OK):**
```json
{
  "url": "https://bucket.sgp1.digitaloceanspaces.com/123_photo.jpg",
  "fileName": "1743580800000_photo.jpg",
  "success": true,
  "message": "Амжилттай хуулагдлаа"
}
```

### GET /files/health
Сервис ажиллаж байгааг шалгах.
