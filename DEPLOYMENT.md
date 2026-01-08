# MCP Test Server - Hướng Dẫn Triển Khai

## 📋 Mục Lục

Document này hướng dẫn quy trình triển khai MCP Test Server từ môi trường development đến production.

## 🏗️ Kiến Trúc Triển Khai

### Development Environment
```
Development Machine
├── JDK 17+
├── Maven 3.6+
├── Git
├── IDE (IntelliJ/Eclipse/VS Code)
└── Terminal/Command Line
```

### Staging Environment
```
Staging Server
├── Docker & Docker Compose
├── PostgreSQL Database
├── Nginx Reverse Proxy
├── SSL Certificate
└── Monitoring Tools
```

### Production Environment
```
Production Cluster
├── Load Balancer (Nginx/HAProxy)
├── Multiple Application Servers
├── PostgreSQL Cluster
├── Redis Cache
├── Monitoring Stack (Prometheus/Grafana)
├── Log Aggregation (ELK)
└── Backup Systems
```

## 🚀 Quy Trình Triển Khai

### Phase 1: Chuẩn Bị Môi Trường

#### 1.1. Server Requirements
```bash
# Minimum requirements cho production server
CPU: 2 cores
RAM: 4GB
Storage: 20GB SSD
Network: 100Mbps
OS: Ubuntu 20.04+ / CentOS 8+
```

#### 1.2. Software Installation
```bash
# Update system
sudo apt update && sudo apt upgrade -y

# Install Java 17
sudo apt install openjdk-17-jdk -y

# Install Maven
sudo apt install maven -y

# Install Git
sudo apt install git -y

# Install Docker
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh

# Install Docker Compose
sudo curl -L "https://github.com/docker/compose/releases/download/v2.20.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
```

#### 1.3. Firewall Configuration
```bash
# Mở ports cho application
sudo ufw allow 22/tcp    # SSH
sudo ufw allow 80/tcp    # HTTP
sudo ufw allow 443/tcp   # HTTPS
sudo ufw allow 5432/tcp # PostgreSQL
sudo ufw enable
```

### Phase 2: Database Setup

#### 2.1. PostgreSQL Installation
```bash
# Install PostgreSQL
sudo apt install postgresql postgresql-contrib -y

# Start và enable service
sudo systemctl start postgresql
sudo systemctl enable postgresql

# Secure PostgreSQL
sudo -u postgres psql -c "ALTER USER postgres PASSWORD 'your_secure_password';"

# Create database và user
sudo -u postgres psql << EOF
CREATE DATABASE mcp_test_db;
CREATE USER mcp_user WITH PASSWORD 'mcp_password';
GRANT ALL PRIVILEGES ON DATABASE mcp_test_db TO mcp_user;
\q
EOF
```

#### 2.2. Database Configuration
```bash
# Configure PostgreSQL
sudo nano /etc/postgresql/14/main/postgresql.conf

# Important settings:
listen_addresses = 'localhost,10.0.0.1'
max_connections = 200
shared_buffers = 256MB
effective_cache_size = 1GB
maintenance_work_mem = 64MB
checkpoint_completion_target = 0.9
wal_buffers = 16MB
default_statistics_target = 100
```

#### 2.3. Database Backup Setup
```bash
# Create backup script
cat > /home/mcp-user/backup-db.sh << 'EOF'
#!/bin/bash
DATE=$(date +%Y%m%d_%H%M%S)
BACKUP_DIR="/var/backups/postgresql"
DB_NAME="mcp_test_db"

mkdir -p $BACKUP_DIR
pg_dump -h localhost -U mcp_user -d $DB_NAME > $BACKUP_DIR/mcp_backup_$DATE.sql
gzip $BACKUP_DIR/mcp_backup_$DATE.sql

# Delete backups older than 7 days
find $BACKUP_DIR -name "*.sql.gz" -mtime +7 -delete
EOF

chmod +x /home/mcp-user/backup-db.sh

# Setup cron job
echo "0 2 * * * /home/mcp-user/backup-db.sh" | sudo crontab -
```

### Phase 3: Application Deployment

#### 3.1. Build Application
```bash
# Clone repository
git clone https://github.com/your-org/mcp-test-server.git
cd mcp-test-server

# Build cho production
mvn clean package -DskipTests -Pprod

# Verify build
ls -la target/test-server-*.jar
```

#### 3.2. Docker Deployment
```bash
# Build Docker image
docker build -t mcp-test-server:1.0.0 .

# Save image to registry
docker tag mcp-test-server:1.0.0 your-registry/mcp-test-server:1.0.0
docker push your-registry/mcp-test-server:1.0.0

# Deploy với Docker Compose
docker-compose -f docker-compose.prod.yml up -d

# Verify deployment
docker-compose ps
docker-compose logs mcp-test-server
```

#### 3.3. Environment Variables
```bash
# Production environment file
cat > .env << EOF
# Database Configuration
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/mcp_test_db
SPRING_DATASOURCE_USERNAME=mcp_user
SPRING_DATASOURCE_PASSWORD=mcp_password
SPRING_JPA_HIBERNATE_DDL_AUTO=update

# Application Configuration
SPRING_PROFILES_ACTIVE=prod
SERVER_PORT=8080
LOGGING_LEVEL_COM_MCP_TESTSERVER=INFO

# Security
SERVER_SSL_ENABLED=true
SERVER_SSL_KEY_STORE=classpath:keystore.p12
SERVER_SSL_KEY_STORE_PASSWORD=your_keystore_password
SERVER_SSL_KEY_STORE_TYPE=PKCS12

# External Integrations
INTEGRATION_JIRA_URL=https://your-company.atlassian.net
INTEGRATION_JIRA_USERNAME=mcp-service@your-company.com
INTEGRATION_JIRA_TOKEN=your_jira_token

INTEGRATION_CONFLUENCE_URL=https://your-company.atlassian.net/wiki
INTEGRATION_CONFLUENCE_USERNAME=mcp-service@your-company.com
INTEGRATION_CONFLUENCE_TOKEN=your_confluence_token
EOF
```

### Phase 4: Web Server Configuration

#### 4.1. Nginx Installation
```bash
# Install Nginx
sudo apt install nginx -y

# Start và enable service
sudo systemctl start nginx
sudo systemctl enable nginx

# Test configuration
sudo nginx -t
```

#### 4.2. SSL Certificate Setup
```bash
# Tạo SSL certificate directory
sudo mkdir -p /etc/nginx/ssl

# Option 1: Let's Encrypt (Recommended)
sudo apt install certbot python3-certbot-nginx -y
sudo certbot --nginx -d your-domain.com --email admin@your-domain.com --agree-tos --non-interactive

# Option 2: Self-signed certificate (Development only)
sudo openssl req -x509 -nodes -days 365 -newkey rsa:2048 \
    -keyout /etc/nginx/ssl/key.pem \
    -out /etc/nginx/ssl/cert.pem \
    -subj "/C=VN/ST=HCM/L=Ho Chi Minh City/O=Your Company/OU=IT Department/CN=your-domain.com"
```

#### 4.3. Nginx Configuration
```bash
# Backup original configuration
sudo cp /etc/nginx/sites-available/default /etc/nginx/sites-available/default.backup

# Deploy MCP Test Server configuration
sudo cp nginx.conf /etc/nginx/sites-available/mcp-test-server

# Enable site
sudo ln -s /etc/nginx/sites-available/mcp-test-server /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl reload nginx
```

### Phase 5: Monitoring và Logging

#### 5.1. Application Monitoring
```bash
# Install Prometheus
sudo apt install prometheus -y

# Configure Prometheus
cat > /etc/prometheus/prometheus.yml << EOF
global:
  scrape_interval: 15s

scrape_configs:
  - job_name: 'mcp-test-server'
    static_configs:
      - targets: ['localhost:8080']
    metrics_path: '/actuator/prometheus'
    scrape_interval: 5s
EOF

# Start Prometheus
sudo systemctl start prometheus
sudo systemctl enable prometheus
```

#### 5.2. Log Management
```bash
# Tạo log directory structure
sudo mkdir -p /var/log/mcp-test-server/{app,access,error}
sudo chown -R syslog:adm /var/log/mcp-test-server

# Configure log rotation
sudo cat > /etc/logrotate.d/mcp-test-server << EOF
/var/log/mcp-test-server/*.log {
    daily
    missingok
    rotate 30
    compress
    delaycompress
    notifempty
    create 644 syslog adm
    postrotate
        systemctl reload mcp-test-server
}
EOF
```

#### 5.3. Health Checks
```bash
# Tạo health check script
cat > /usr/local/bin/mcp-health-check.sh << 'EOF'
#!/bin/bash
HEALTH_URL="http://localhost:8080/mcp-test-server/api/mcp/health"
RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" $HEALTH_URL)

if [ $RESPONSE -eq 200 ]; then
    echo "$(date): MCP Test Server is healthy"
    exit 0
else
    echo "$(date): MCP Test Server is unhealthy (HTTP $RESPONSE)"
    # Restart service
    docker-compose restart mcp-test-server
    exit 1
fi
EOF

chmod +x /usr/local/bin/mcp-health-check.sh

# Add to cron
echo "*/5 * * * * /usr/local/bin/mcp-health-check.sh" | sudo crontab -
```

## 🔧 Configuration Files

### Production Docker Compose
```yaml
# docker-compose.prod.yml
version: '3.8'

services:
  mcp-test-server:
    image: your-registry/mcp-test-server:1.0.0
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/mcp_test_db
      - SPRING_DATASOURCE_USERNAME=mcp_user
      - SPRING_DATASOURCE_PASSWORD=mcp_password
    volumes:
      - /var/log/mcp-test-server:/app/logs
      - /etc/ssl:/app/ssl
    depends_on:
      - postgres
    restart: unless-stopped
    deploy:
      replicas: 2
      resources:
        limits:
          cpus: '1.0'
          memory: 1G
        reservations:
          cpus: '0.5'
          memory: 512M

  postgres:
    image: postgres:15-alpine
    environment:
      - POSTGRES_DB=mcp_test_db
      - POSTGRES_USER=mcp_user
      - POSTGRES_PASSWORD=mcp_password
    volumes:
      - postgres_data:/var/lib/postgresql/data
      - ./backup:/backup
    restart: unless-stopped
    deploy:
      resources:
        limits:
          cpus: '0.5'
          memory: 512M

  nginx:
    image: nginx:alpine
    ports:
      - "80:80"
      - "443:443"
    volumes:
      - ./nginx.prod.conf:/etc/nginx/nginx.conf
      - /etc/letsencrypt:/etc/nginx/ssl
      - /var/log/nginx:/var/log/nginx
    depends_on:
      - mcp-test-server
    restart: unless-stopped

volumes:
  postgres_data:
```

### Systemd Service
```ini
# /etc/systemd/system/mcp-test-server.service
[Unit]
Description=MCP Test Server
After=docker.service
Requires=docker.service

[Service]
Type=oneshot
RemainAfterExit=yes
WorkingDirectory=/opt/mcp-test-server
ExecStart=/usr/bin/docker-compose up -d
ExecStop=/usr/bin/docker-compose down
TimeoutStartSec=0

[Install]
WantedBy=multi-user.target
```

## 🔍 Verification và Testing

### Health Check Commands
```bash
# Kiểm tra application status
curl -f http://localhost:8080/mcp-test-server/api/mcp/health

# Kiểm tra database connection
docker-compose exec postgres psql -U mcp_user -d mcp_test_db -c "SELECT COUNT(*) FROM documents;"

# Kiểm tra logs
docker-compose logs -f mcp-test-server

# Kiểm tra resource usage
docker stats mcp-test-server
```

### Load Testing
```bash
# Install Apache Bench
sudo apt install apache2-utils -y

# Load test
ab -n 1000 -c 10 http://localhost/mcp-test-server/api/mcp/health

# Alternative với wrk
wrk -t12 -c400 -d30s http://localhost/mcp-test-server/api/mcp/health
```

## 🚨 Troubleshooting

### Common Issues và Solutions

#### 1. Application không start
```bash
# Kiểm tra logs
docker-compose logs mcp-test-server

# Kiểm tra port conflicts
sudo netstat -tlnp | grep :8080

# Kiểm tra memory usage
docker stats mcp-test-server
```

#### 2. Database connection issues
```bash
# Kiểm tra PostgreSQL status
sudo systemctl status postgresql

# Test connection
psql -h localhost -U mcp_user -d mcp_test_db -c "SELECT version();"

# Kiểm tra configuration
sudo postgres -U postgres -c "\l"
```

#### 3. SSL Certificate issues
```bash
# Kiểm tra certificate validity
openssl x509 -in /etc/nginx/ssl/cert.pem -text -noout

# Test SSL configuration
sudo nginx -t

# Renew Let's Encrypt certificate
sudo certbot renew --dry-run
```

#### 4. Performance issues
```bash
# Kiểm tra system resources
top
htop
free -h
df -h

# Profile application
java -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -jar target/test-server-1.0.0.jar
```

## 📊 Performance Tuning

### Database Optimization
```sql
-- PostgreSQL performance tuning
ALTER SYSTEM SET shared_buffers = '256MB';
ALTER SYSTEM SET effective_cache_size = '1GB';
ALTER SYSTEM SET maintenance_work_mem = '64MB';
ALTER SYSTEM SET checkpoint_completion_target = 0.9;
ALTER SYSTEM SET wal_buffers = '16MB';
ALTER SYSTEM SET default_statistics_target = 100;
```

### JVM Tuning
```bash
# Production JVM options
JAVA_OPTS="-Xmx2g -Xms1g -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:+UseStringDeduplication -XX:+OptimizeStringConcat"

# GC Logging
JAVA_OPTS="$JAVA_OPTS -Xlog:gc*:file=/var/log/mcp-test-server/gc.log:time,uptime,level,tags"
```

## 🔄 CI/CD Pipeline

### GitHub Actions
```yaml
# .github/workflows/deploy.yml
name: Deploy MCP Test Server

on:
  push:
    branches: [main]
  pull_request:
    branches: [main]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'
      - name: Cache Maven packages
        uses: actions/cache@v3
        with:
          path: ~/.m2
          key: ${{ runner.os }}-m2-${{ hashFiles('**/pom.xml') }}
      - name: Run tests
        run: mvn clean test
      - name: Build application
        run: mvn clean package -DskipTests

  deploy:
    needs: test
    runs-on: ubuntu-latest
    if: github.ref == 'refs/heads/main'
    steps:
      - name: Deploy to production
        run: |
          # Deploy commands
          ssh user@your-server "cd /opt/mcp-test-server && git pull && docker-compose up -d"
```

## 📋 Maintenance và Updates

### Rolling Updates
```bash
#!/bin/bash
# rolling-update.sh
NEW_VERSION=$1
CURRENT_VERSION=$(docker-compose images mcp-test-server | grep -oP 'mcp-test-server:\K[0-9]+\.[0-9]+\.[0-9]+')

echo "Current version: $CURRENT_VERSION"
echo "New version: $NEW_VERSION"

# Backup current version
docker tag mcp-test-server:latest mcp-test-server:backup-$(date +%Y%m%d_%H%M%S)

# Pull new version
docker pull your-registry/mcp-test-server:$NEW_VERSION

# Update docker-compose.yml
sed -i "s/mcp-test-server:latest/mcp-test-server:$NEW_VERSION/g" docker-compose.yml

# Rolling update
docker-compose up -d --no-deps mcp-test-server

# Health check
sleep 30
curl -f http://localhost:8080/mcp-test-server/api/mcp/health

# Cleanup old version if healthy
if [ $? -eq 0 ]; then
    docker rmi mcp-test-server:backup-$(date +%Y%m%d_%H%M%S)
fi
```

### Backup Strategy
```bash
#!/bin/bash
# backup-mcp.sh
BACKUP_DIR="/backup/mcp-test-server/$(date +%Y%m%d)"
DATE=$(date +%Y%m%d_%H%M%S)

mkdir -p $BACKUP_DIR

# Backup database
docker-compose exec postgres pg_dump -U mcp_user mcp_test_db > $BACKUP_DIR/database_$DATE.sql

# Backup application
docker cp mcp-test-server:/app/logs $BACKUP_DIR/logs_$DATE

# Backup configuration
cp docker-compose.yml $BACKUP_DIR/docker-compose_$DATE.yml
cp nginx.conf $BACKUP_DIR/nginx_$DATE.conf

# Compress backup
tar -czf $BACKUP_DIR.tar.gz $BACKUP_DIR
rm -rf $BACKUP_DIR

echo "Backup completed: $BACKUP_DIR.tar.gz"
```

---

## 📞 Emergency Procedures

### Disaster Recovery
1. **Database Recovery**: Restore từ backup gần nhất
2. **Application Recovery**: Deploy version trước đó
3. **Configuration Recovery**: Restore từ version control
4. **Data Integrity**: Verify với checksums

### Contact Information
- **On-call Engineer**: +84-xxx-xxx-xxx
- **Team Lead**: +84-xxx-xxx-xxx
- **DevOps**: +84-xxx-xxx-xxx
- **Management**: +84-xxx-xxx-xxx

---

**Lưu ý**: Luôn test trong môi trường staging trước khi deploy production!
