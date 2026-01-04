# sparta-msa-project-part-4
# 010-6355-0806 송영진


# 1. Docker 기본 개념 및 클라우드 개념 비교 리서치
## 클라우드 환경 (AWS/GCP/AZure)에서 컨테이너가 어떻게 활용되는지 조사
### [GCP]
- Google Kubernetes Engine
    - 컨테이너에서 앱을 실행하기 위한 사용하기 쉽고 신뢰할 수 있는 Kubernetes 서비스
- Cloud Build
    - 컨테이너에서 앱을 빠르게 빌드, 테스트, 배포할 수 있는 서비스
- Cloud Run
    - 원하는 언어를 사용하여 원하는 방식으로 코드를 작성하고 컨테이너에 앱을 배포
- Container Registry
    - Docker 컨테이너 이미지를 저장, 관리, 보호
- Cloud Code
    - 컨테이너화된 앱의 작성, 구동, 디버깅을 위한 통합 개발 환경
- Deep Learning Containers
    - 데이터 과학 프레임워크, 라이브러리, 도구가 포함된 컨테이너
- 클라우드 기반 앱 개발 (솔루션)
- 메인프레임 현대화 (솔루션)
    - GKE를 사용하여 메인프레임 환경에 정속되어 있던 기존 워크로드를 컨테이너로 이전할 수 있음
### [AWS]
- Amazon EC2와 함께 사용할 서비스
    - Amazon EC2 AUto Scaling
        - 어플리케이션의 로드를 처리할 수 있는 정확한 수의 Amazon EC2 인스턴스를 유지하는데 도움
    - AWS Backup
        - Amazon EC2 인스턴스와 여기에 연결된 Amazon EBS 볼륨의 백업을 자동화
    - Amazon CloudWatch
        - 인스턴스와 Amazon EBS 볼륨을 모니터링
    - Elastic Load Balancing
        - 수신되는 어플리케이션 트래픽을 여러 인스턴스로 자동 분산
    - Amazon GuardDuty
        - EC2 인스턴스에 대한 잠재적 무단 사용 또는 악의적 사용을 탐지
    - EC2 Image Builder
        - 사용자 지정되어 안전하고 최신 상태인 서버 이미지의 생성, 관리 및 배포를 자동화
    - AWS Launch Wiard
        - 개별 AWS 리소스를 수동으로 식별하고 프로비저닝할 필요 없이 타사 어플리케이션에 대한 AWS 리소스를 크기 조정, 구성 및 배포
    - AWS Systems Manager
        - EC2 인스턴스에서 대규모로 작업을 수행
- Amazon EC2를 사용하는 대신 다른 AWS 컴퓨팅 서비스를 사용하여 인스턴스 시작
    - Amazon Lightsail
        - 저렴하고 예측 가능한 월별 요금으로 프로젝트를 신속하게 배포하는데 필요한 리소스를 제공하는 클라우드 플랫폼
    - Amazon Elastic Container Service (Amazon ECS)
        - EC2 인스턴스 클러스터에서 컨테이너화된 어플리케이션을 배포, 관리하고 규모를 조정
    - Amazon Elastic Kubernetes Service (Amazon EKS)
        - AWS에서 Kubernetes 어플리케이션을 실행
### [AZure]
- App Configuration
    - 앱 구성을 위한 빠륵도 확장성 있는 매개 변수 스토리지
- Azure Container Storage
    - 상태 저장 컨테이너 어플리케이션을 위한 영구 볼륨 관리
- Azure Red Hat OpenShift
    - Red Hat과 공동 운영되는 완전 관리형 OpenShift 서비스
- Azure Container Apps
    - 서버리스 컨테이너를 사용하여 최신 앱 및 마이크로 서비스 빌드 및 배포
- Azure Functions
    - 서버가 없는 코드로 이벤트 처리
- Azure Service Fabric
    - Windows 또는 Linux에서 마이크로 서비스를 개발하고 컨테이너를 오케스트레이션
- Azure Container Instances
    - 서버를 관리하지 않고 쉽게 Azuredptj zjsxpdlsj tlfgod
- AzureKubernetes Fleet Manager
    - Azure Kubernetes Service 클러스터에 대해 다중 클러스터 및 대규모 시나리오 사용
- Web App for Containers
    - 비지니스에 맞게 크기 조정하도록 컨테이너화된 웹앱을 쉽게 배포 및 실행
- Azure Container Registry
    - 모든 유형의 Azure 배포에서 컨테이너 이미지 저장 및 관리
- Azure Kubernetes Service (AKS)
    - 배포, 관리 및 Kubernetes 작업 간소화
## Docker 주요 개념은 무엇인가?
- 컨테이너 기술을 활용하여 어플리케이션을 실행하는 플랫폼
- 가상 머신과 비교하여 가볍고 빠른 환경 제공
- Image
    - 실행 가능한 어플리케이션 패키지
    - 컨테이너를 실행하는데 필요한 파일 시스템과 어플리케이션 코드가 포함된 탬플릿
    - lay 기반의 구조
    - 같은 이미지에서 여러 개의 컨테이너 생성 가능
- Container
    - 실행 중인 독립된 환경
    - 자체 파일 시스템과 네트워크 인터페이스를 가지며
    - 다른 컨테이너와 격리됨
- Dockerfile
    - 컨테이너를 정의하는 설정 파일
    - image를 생성하는데 기본이 되는 파일
- Docker Hub
    - 공개된 Docker 이미지를 저장하는 레파지토리
## VM과 컨테이너의 차이점은 무엇인가?
- 차이점
    - 컨테이너는 VM보다 훨씬 더 경량입니다.
    - 컨테이너는 OS 수준에서 가상화되고 VM은 하드웨어 수준에서 가상화됩니다.
    - 컨테이너는 OS 커널을 공유하며 VM에 필요한 것보다 훨씬 적은 메모리를 사용합니다.

## 클라우드에서 Docker를 사용하는 이유는 무엇인가?
