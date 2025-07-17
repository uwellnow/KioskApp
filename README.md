## 안드로이스 스튜디오 에뮬레이터 정보
- 1920x1080 해상도, 27인치 -> 81.6 dpi (코드에서는 81dpi로 계산해서 크기 적용중)
- 화면 방향 : landscape
- Android 11 (Api 30)
- Phone/Tablet 기종으로 선택 (TV로 선택하면 에뮬레이터 강제종료됨)
- 안드로이드 스튜디오 버전은 최신 버전 (NarWhal)
- RAM: 2GB
- 저장공간: 16GB

## 개발 진행 상황(2025.07.17.수정)
### 완료됨 
- 앱 내부 상태 관리 ViewModel 구현 (앱이 켜져 있는 동안 상태 저장)
  - 메뉴 선택 -> 주문 내역(장바구니)
  - navigation 설정 (화면 전환)
 
### 해야하는 것 
- 외부 DB 연결 필요(현재는 mockData로 테스트 중)
  - 회원 정보(고유 id, 이름 or 이메일, 전화번호, 잔여 잔 수)
  - 등록되어 있는 제품 정보(로고 이미지, 제품 이미지, 제품 이름, 상세 정보, 영양 정보, 섭취 시간(운동 전or중-or후)
- 관리자가 팔로우업 할 수 있는 서버 구축
- 외부 단말기 및 qr(바코드) 등 통신 연결
- 그림자, 글꼴, 애니메이션, 오류 모달 창 등 추가 UI 수정 필요

## 디렉토리 구조 및 파일 설명
```
StrongLife/
  └─ app/
      ├─ src/
      │   └─ main/
      │       ├─ java/com/app/stronglife/
      │       │   ├─ data/
      │       │   │   └─ model/           # 데이터 모델 정의 (CartItem, Member, Product 등)
      │       │   ├─ mock/                # 목업 데이터 (테스트용 가짜 데이터)
      │       │   ├─ navigation/          # 네비게이션 그래프 (화면 이동 관리)
      │       │   ├─ ui/
      │       │   │   ├─ component/       # 공통 UI 컴포넌트 (TopBar 등)
      │       │   │   └─ screen/
      │       │   │       ├─ CartScreen/      # 장바구니 화면 및 관련 컴포넌트
      │       │   │       ├─ EndScreen/       # 결제 완료 등 마지막 화면
      │       │   │       ├─ firstScreen/     # 첫 시작 화면 (번호 입력, 시간 선택 등)
      │       │   │       ├─ menuScreen/      # 메뉴 리스트, 메뉴 상세, 메뉴 추가/장바구니 이동 등
      │       │   │       ├─ PayingScreen/    # 결제 진행 중 화면
      │       │   │       ├─ PayScreen/       # 결제 방식 선택 및 결제 UI
      │       │   │       └─ PaySelectScreen/ # 결제 수단 선택 화면
      │       │   ├─ viewmodel/           # 각 화면별 ViewModel (상태 관리)
      │       └─ res/                     # 리소스(이미지, 폰트, 레이아웃, 문자열 등)
      ├─ build.gradle.kts
      └─ proguard-rules.pro
  ├─ build.gradle.kts
  ├─ gradle/
  ├─ gradlew
  ├─ gradlew.bat
  └─ settings.gradle.kts
```
- firstScreen: 앱의 첫 화면.
- menuScreen: 메뉴 리스트, 메뉴 상세, 메뉴 추가, 장바구니 이동 등 메뉴 관련 화면.
- CartScreen: 장바구니에 담긴 상품 목록, 수량 조절, 삭제 등 장바구니 기능 제공.
- PaySelectScreen: 결제 수단(카드, QR) 선택 화면.
- PayScreen: 실제 결제 진행 화면. 결제 방식별 UI(QR, 휴대폰 결제 등) 포함. (방식 추가 -> UI 수정 예정)
- PayingScreen: 결제 처리 중임을 보여주는 화면.
- EndScreen: 결제 완료 등 마지막 안내 화면.
- component: 여러 화면에서 공통으로 사용하는 UI 컴포넌트(TopBar)
- data/model: CartItem, Member, Product 등 데이터 모델 정의.
- mock: 테스트용 목업 데이터(이게 DB로 가야함)
- navigation: 앱 내 화면 이동(네비게이션) 관리.
- viewmodel: 각 화면별 상태 관리(ViewModel).
