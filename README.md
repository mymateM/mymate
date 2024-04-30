# 함께 쓰는 평화로운 공동 가계부 마이메이트
![마이메이트 앱 소개](https://github.com/mymateM/mymate/assets/83498457/ad404b95-b1fc-459a-93bb-56678e245252)

# 프로젝트 소개
* 마이메이트는 공동 생활을 하는 사람들이 생활비를 정산하고 관리할 수 있도록 도와주는 공동 가계부 어플리케이션입니다.
* 가구원별 생활비 부담 비율과 정산일을 설정할 수 있습니다. 설정한 정산일이 되면 가구원에게 송금하거나 송금받을 금액을 고지받고, 가구원의 계좌를 복사하거나 송금 요청 알림을 보낼 수 있습니다. 
* 가구의 지출 목표 금액을 설정할 수 있습니다. 생활비를 어플리케이션에 등록하면 목표 금액 대비 사용 금액 현황을 알려줍니다. 달별 지출 현황과 카테고리별 지출 추이 등의 통계를 확인할 수 있습니다.
* 고지서를 등록하여 관리할 수 있습니다. 직접 입력하거나 내장 OCR 기능을 사용하여 스캔하는 방법으로 간편하게 입력할 수 있습니다.

# 팀원 구성
| 정가은 | 윤영서 | 구지현 | 백은희 |
|-|-|-|-|
| 안드로이드 | UI/UX | GUI | 백엔드 |
| 어플리케이션 화면 동작 <br> OCR 기능 / 서버와의 통신 <br> 소셜로그인 <br> 알람 시스템 <br> 안드로이드 개발 전반 | 화면 와이어프레임 <br> 사용자 경험 / 가치 설정 <br> 화면 동작 설계 <br> 유저 플로우 설계 등 | 브랜드 컬러 / 캐릭터 / 폰트 설정 등 <br> 화면 세부 디자인 <br> 그래픽 요소 디자인 등 | 서버 / 데이터베이스 구축 <br> 안드로이드와의 통신(API 작성) <br> 데이터 처리 및 연산 <br> 백엔드 개발 전반 |
| [@esfjge](https://github.com/EunaJ99/EunaJ99) | | | [@ehBeak](https://github.com/ehBeak) | 

# 개발 기간 및 작업 관리
### 개발 기간 
* 기획 및 프로토타입 개발 : 2023-03-02 ~ 2023-06-21
* 기획 / 디자인 / 개발 보강 및 정리 : 2023-06-22 ~ 2023-08-31
* 서비스용 어플리케이션 개발 : 2023-09-01 ~ 2023-11-27

### 작업 관리
* 디자인은 Figma에서 관리하여 수정사항을 실시간으로 확인할 수 있도록 했습니다.
* 개발은 팀원이 없이 부서장만 있는 관계로 개인화하여 작업했습니다.
* 주간회의를 진행하며 작업 순서와 방향성, 일정에 관한 고민과 보고를 나누고 Notion에 기록했습니다.

**이 레포지토리는 정가은(@esfjge)이 담당한 안드로이드 개발 부분의 코드만 포함하고 있습니다.** 백엔드 기술이 궁금하시다면 [mymateM/AcountApp](https://github.com/mymateM/AcountApp)으로 이동해주세요. 아래부터는 안드로이드 개발 부분에 한정하여 서술하고 있습니다.

# 개발 환경
* 사용 언어 : Kotlin
* IDE : Android Studio
* 협업 툴 : Notion, Discord, Figma
* 테스트 기기 : 갤럭시 A32

## 사용 라이브러리
* 소셜 로그인 : Google, Kakao, Naver
* 알람 시스템 : Firebase Cloud Messaging
* 서버 통신 : Retrofit2
* OCR(머신러닝) : ML Kit
* 파이차트 : MPAndroidChart
* 이미지 뷰 : PhotoView

# 페이지별 기능
### 초기화면
* 서비스 접속 초기화면입니다. splash 화면이 잠시 나온 뒤 다음 페이지가 나타납니다.
  * 로그인이 되어 있지 않은 경우 : 로그인 페이지
  * 로그인이 되어 있는 경우 : 탭 4개로 구성된 홈 화면

| 초기화면 |
| - |
| ![splash](https://github.com/mymateM/mymate/assets/83498457/e753cd10-6b7c-42ab-bc0a-55885225ca5c) |

### 로그인
* 소셜 로그인을 지원합니다. 네이버, 카카오, 구글 계정을 통해 로그인할 수 있습니다.
* 로컬 로그인 역시 지원합니다. 로컬 로그인 버튼을 누르면 이메일과 비밀번호를 입력하는 화면으로 넘어갑니다.
  * 이메일은 '@'문자 포함 여부를 통해 유효성 검사를 진행합니다.
  * 비밀번호는 정규식을 활용해 유효성 검사를 진행합니다.

| 로그인 초기화면 |
|-|
| ![login](https://github.com/mymateM/mymate/assets/83498457/81f7e8cb-42a8-421a-b4c3-73ccea5d31b6) |

| 로컬 로그인 - 입력 전 | 로컬 로그인 - 입력 완료 |
| - | - |
| ![locallogin](https://github.com/mymateM/mymate/assets/83498457/c5f6d15b-37d2-487c-b82d-9f4640009104) | ![locallogin2](https://github.com/mymateM/mymate/assets/83498457/539a61af-3834-4ad6-a807-d56c9b72b363) |

### 회원가입(온보딩)
* 소셜 회원가입은 각 소셜 기업에서 지원하고 있습니다.
* 로컬 회원가입은 이용약관 동의 - 이메일 입력 - 비밀번호 설정 - 온보딩(계정설정) 순으로 이루어집니다.
* 계정설정 단계에서 사용자가 선택하거나 입력해야 하는 정보는 다음과 같습니다.
  * 프로필을 설정하고 닉네임을 입력해야 합니다.
  * 정산용 계좌와 은행 정보를 입력해야 합니다.
  * 초대받은 사람인지, 초대할 사람인지 선택해야 합니다.
    * 사용자가 다른 사용자를 초대한다고 답하면 초대코드가 생성됩니다. 사용자는 이것을 복사하여 다른 사람을 초대할 수 있습니다.
    * 초대할 사람은 가구의 이름, 정산일, 예산을 추가로 설정해야 합니다.
* 회원가입과 온보딩 화면은 버튼 클릭 / 정보 입력 여부에 따라 화면 요소의 색이 바뀌는 등 사용자의 이해를 돕기 위한 인터랙티브 기능이 포함되어 있습니다.
* 비밀번호의 경우, 사용자가 입력한 비밀번호가 설정된 조건에 맞으면 자동으로 조건이 감춰지고 비밀번호 확인 버튼이 나타납니다.

| 회원가입 - 약관 동의 전 | 회원가입 - 약관 동의 후 |
| - | - |
| ![signup_terms_1](https://github.com/mymateM/mymate/assets/83498457/56a2f657-7ce4-4000-bf2c-76d3905840e1) | ![signup_terms_3](https://github.com/mymateM/mymate/assets/83498457/0af0c754-422e-4bb2-ba5c-ac3d2df35cdb) |

| 회원가입 - 이메일 입력 전 | 회원가입 - 이메일 입력 후|
| - | - |
| ![signup_email_1](https://github.com/mymateM/mymate/assets/83498457/42d580c6-545f-47c6-8f3e-fe2d2d44d469) | ![signup_email_2](https://github.com/mymateM/mymate/assets/83498457/70a319de-e2a1-4551-87f1-c9df98430589) |

| 회원가입 - 비밀번호 입력 | 회원가입 - 비밀번호 확인란 입력 |
| - | - |
| ![signup_pw_2](https://github.com/mymateM/mymate/assets/83498457/ce0d5a25-f158-425e-9aa7-85c0c307751b) | ![signup_pw_4](https://github.com/mymateM/mymate/assets/83498457/1088d3c4-da6f-4463-8e41-ddf1278d4465) |

| 온보딩 - 프로필 설정(1) | 온보딩 - 프로필 설정(2) |
| - | - |
| ![profile_onboarding](https://github.com/mymateM/mymate/assets/83498457/f3650ac7-05a0-432d-b664-907f26c2a2f2) | ![profile_onboarding_2](https://github.com/mymateM/mymate/assets/83498457/5e307c97-4373-47b4-809d-eb46b24ca6c8) |

| 온보딩 - 계좌 설정(1) | 온보딩 - 계좌 설정(2) |
| - | - |
| ![onboarding_account(1)](https://github.com/mymateM/mymate/assets/83498457/e7986248-6100-4c66-bc9c-5eb16ba4bc44) | ![onboarding_account(2)](https://github.com/mymateM/mymate/assets/83498457/2bdf454c-0d66-4e16-ab1e-84ad2c68afa9) |

| 온보딩 - 초대코드 초기화면 |
| - |
| ![invite](https://github.com/mymateM/mymate/assets/83498457/447ea6e8-7dad-43aa-88be-694db0419f79) 

사용자는 다른 사용자에게 초대받은 입장인지, 자신이 새로 가구를 만들어 초대해야 할 입장인지를 선택하여 응답합니다. 다른 사용자에게 초대받았다면 초대 코드를 입력해야 합니다.

| 온보딩 - 초대코드 입력화면 | 온보딩 - 초대 완료 |
| - | - |
| ![invited](https://github.com/mymateM/mymate/assets/83498457/93a5a6c2-8276-4726-a293-9a51915d1f40) | ![welcome](https://github.com/mymateM/mymate/assets/83498457/6946a661-d84b-4e8a-a155-d0b25ad8883e) |

사용자가 다른 사용자를 초대한다고 말하면, 초대 코드가 생성됩니다. 사용자는 이후 가구 설정을 이어가야 합니다.

| 온보딩 - 초대코드 생성화면 |
| - |
| ![invitation](https://github.com/mymateM/mymate/assets/83498457/1764686f-d378-4706-a207-9fffd1c40cc7) |

| 온보딩 - 가구 이름 설정 | 온보딩 - 정산일 설정 |
| - | - |
| ![housename](https://github.com/mymateM/mymate/assets/83498457/1ac028db-daf0-411a-b757-204444f2fcc4) | ![housesettledate](https://github.com/mymateM/mymate/assets/83498457/5f2e58dc-28d3-43d5-bdb1-44a936622c90) |

| 온보딩 - 가구 예산 설정 | 온보딩 - 가구 설정 완료 |
| - | - |
| ![housebudget](https://github.com/mymateM/mymate/assets/83498457/45a1d565-3345-48c6-9137-a4bf5307d4fa) | ![housefinished](https://github.com/mymateM/mymate/assets/83498457/d53a1d9e-04eb-4acb-962f-6f65d51f1133) |

### 홈
* 사용자가 가장 많이 보게 될 홈 화면입니다. 메인 화면을 구성하고 있는 4개의 탭 중 첫 번째입니다.
* 지금까지 사용한 생활비 금액, 금액이 예산에서 차지하는 비율, 지난 달과 이번 달의 소비 현황 비교, 남은 예산 등을 보여줍니다.
* 그래프에 사용되는 수치는 백엔드(데이터베이스)에서 받아옵니다. 계산도 마찬가지로 백엔드에서 수행합니다. 그래프는 안드로이드에서 가이드라인과 MPAndroidChart 라이브러리를 사용해 구현하였습니다.
* 아래 탭바를 통해 다른 탭으로 이동할 수 있습니다. 오른쪽 상단 아이콘을 클릭해 알림을 확인할 수 있습니다.

| 홈 화면 |
| - |
| ![Home](https://github.com/mymateM/mymate/assets/83498457/b540d112-b877-44b1-8967-58e56403cf69) |

### 알림
* 홈 탭에서 상단 아이콘을 통해 접근할 수 있습니다. 활동 알림과 지출 알림으로 탭이 나뉘어 있습니다.
* 활동 알림에는 가구원의 주요 활동이나 요청, 정산일과 예산 관련 알림이 쌓입니다. 지출 알림에는 지출 내역이 쌓입니다.
* 지출 알림의 경우 홈 화면 아이콘에 새로 알림이 도착했다는 표시가 나타나지 않으며 사용자에게 푸시 알림을 하지 않지만, 알림 탭 내부에서는 새로 도착한 알림 표시를 확인할 수 있습니다.
* 활동 알림의 경우 고지서나 정산에 관련된 알림은 클릭을 통해 관련 화면으로 이동할 수 있습니다.

| 알림 - 활동 알림 | 알림 - 지출 알림 |
| - | - |
| ![alarm_activity](https://github.com/mymateM/mymate/assets/83498457/0bbe5d84-1337-44de-8c1f-5d8ee10263b3) | ![alarm_spending](https://github.com/mymateM/mymate/assets/83498457/705d6ee7-7d0d-4d31-ac7d-b5e1b2cd7340) |

### 가계부
* 메인 화면을 구성하고 있는 4개의 탭 중 두 번째입니다.
* 등록한 생활비 지출 내역을 달력에서 확인할 수 있습니다. 날짜를 클릭해서 다른 날로 이동할 수 있습니다.
* 내역을 등록하거나 삭제할 수 있습니다.
* 상단 아이콘을 통해 고지서 / 검색 화면으로 넘어갈 수 있습니다.

| 가계부 화면 |
| - |
| ![settlement_receive](https://github.com/mymateM/mymate/assets/83498457/704fb3be-d718-4fc2-9a31-5df6c162adc2) |

| 내역 추가 - 전 | 내역 추가 - 카테고리 선택 |
| - | - |
| ![내역 추가](https://github.com/mymateM/mymate/assets/83498457/d8a40606-aa69-47bb-8c45-cd73020f769a) | ![내역 추가 _ 카테고리](https://github.com/mymateM/mymate/assets/83498457/54c95bbd-25c8-4c83-af8a-e7b58b9db8d8) |

| 내역 상세 | 내역 삭제 |
| - | - |
| ![내역 상세](https://github.com/mymateM/mymate/assets/83498457/46405736-7e20-470f-a2a5-7ebdaac00d99) | ![내역 삭제](https://github.com/mymateM/mymate/assets/83498457/cb2b5245-acaf-4432-876b-c68912ac307f) |

### 검색
* 가계부 화면의 우상단 돋보기 아이콘을 통해 접근할 수 있습니다.
* 기간, 카테고리, 가격, 정렬 조건으로 등록된 내역을 검색할 수 있습니다.
* 내역 이름으로 검색하는 기능은 현재 미구현 상태입니다.

| 검색 - 초기화면 | 검색 - 기간 |
| - | - |
| ![ledger_search](https://github.com/mymateM/mymate/assets/83498457/700baeaf-38c0-4056-a967-576e9b530cad) | ![search_terms](https://github.com/mymateM/mymate/assets/83498457/58b14486-0dde-488c-a8e0-deb315956c60) |

| 검색 - 카테고리 | 검색 - 검색완료 |
| - | - |
| ![search_category](https://github.com/mymateM/mymate/assets/83498457/c8f91f54-02a5-493a-abd9-5005d051898e) | ![search_finish](https://github.com/mymateM/mymate/assets/83498457/7ca618c4-f55d-42ab-ad09-50dae536132e) |

### 고지서
* 가계부 화면에 우상단 네모 아이콘을 통해 접근할 수 있습니다.
* 도시가스, 전기, 수도, 기타의 4개 카테고리로 고지서를 모아볼 수 있습니다.
* 고지서를 스캔하여 입력하거나 직접 정보를 입력할 수 있습니다.

| 고지서 - 첫 화면 | 고지서 - 내역 |
| - | - |
| ![고지서](https://github.com/mymateM/mymate/assets/83498457/fdaf232b-1177-415e-b56f-b585e303f889) | ![도시가스 상세](https://github.com/mymateM/mymate/assets/83498457/3df223e2-fe58-4dcc-a18d-b6b6c7b0bb5f) |

| 고지서 - 스캔 과정 | 고지서 - 스캔 결과 |
| - | - |
| ![가로카메라](https://github.com/mymateM/mymate/assets/83498457/827789e2-1fbf-4ee0-8c83-788210d51282) | ![고지서_스캔결과](https://github.com/mymateM/mymate/assets/83498457/77066443-e722-4815-9cf1-8e5e15452944) |

| 고지서 - 사진 스캔 후 | 고지서 - 직접 입력 시 |
| - | - |
| ![고지서 추가 (사진 스캔 후)](https://github.com/mymateM/mymate/assets/83498457/2c72609a-e9cc-41a4-95cb-2361bfbb8533) | ![고지서 추가 (직접 입력)](https://github.com/mymateM/mymate/assets/83498457/edd7df79-0dc1-46fd-8528-398152d760a1) |

스캔된 고지서를 화면에 띄우는 데에는 PhotoView 라이브러리가 사용되었습니다.

입력된 고지서는 가계부에서 지출 내역을 확인하고 삭제한 것과 흡사하게 관리할 수 있습니다.

### 리포트
* 메인 화면을 구성하고 있는 4개의 탭 중 세 번째입니다.
* 전체 가구의 지출과 개인 지출로 나누어 확인할 수 있습니다. 상단의 날짜를 누르면 이전 기록을 확인할 수 있습니다.

| 리포트 - 가구 | 리포트 - 개인 |
| - | - |
| ![reports_house](https://github.com/mymateM/mymate/assets/83498457/3a63d694-79de-4a88-bd70-5733791bb53b) | ![reports_personal](https://github.com/mymateM/mymate/assets/83498457/05b76528-b963-490d-94b7-0e86b777466d) |

### 마이페이지
* 메인 화면을 구성하고 있는 4개의 탭 중 마지막입니다.
* 프로필을 수정하거나 정산 비율, 예산, 정산일, 등록된 계좌를 수정할 수 있습니다.
* 정산 비율, 계좌 정보를 제외한 대부분의 정보 수정은 현재 미구현 상태입니다.
* 프로필 수정과 정산일 수정 화면은 온보딩의 것과 흡사하므로, 정산 비율 수정 / 예산 수정 / 계좌 관리 / 정산일 관리 페이지만 첨부합니다.

| 마이페이지 |
| - |
| ![mypage](https://github.com/mymateM/mymate/assets/83498457/9411eb24-36b5-4e45-bb93-9c5c3a96dd5f) |

| 정산 비율 수정 | 예산 수정 |
| - | - |
| ![정산비율수정](https://github.com/mymateM/mymate/assets/83498457/b0685ad6-79e3-453f-b2a6-74dffa5330bb) | ![예산수정](https://github.com/mymateM/mymate/assets/83498457/11478f03-b7b9-42e3-bbba-d4b44bb1d06e) |

| 계좌 관리 | 정산일 관리 |
| - | - |
| ![계좌관리](https://github.com/mymateM/mymate/assets/83498457/fd2e73cb-e462-40c1-8b2f-c9e5ed1c4553) | ![정산일관리](https://github.com/mymateM/mymate/assets/83498457/d23b028a-f97c-4a90-b30a-76e344b7dfea) |

### 설정
* 마이페이지 상단 톱니바퀴 아이콘을 클릭해 접근할 수 있습니다.
* 현재 기능 미구현 상태입니다.

| 설정 화면 |
| - |
| ![setting](https://github.com/mymateM/mymate/assets/83498457/1c503872-1887-4750-a47c-1df3e6478f80) |


### 정산
* 활동 알림의 정산 관련 알림을 클릭하여 접근할 수 있습니다. 보통 정산일마다 접근하게 됩니다.
* 자신이 전체 생활비 지출액에서 부담해야 하는 금액과 실제로 현재까지 부담한 금액을 그래프로 확인할 수 있습니다.
  * 만일 부담해야 하는 금액보다 덜 냈다면, 다른 가구원에게 돈을 보내야 합니다. 정산할 금액과 대상을 확인하고 계좌를 복사할 수 있습니다.
  * 만일 부담해야 하는 금액보다 더 냈다면, 다른 가구원에게 돈을 받아야 합니다. 정산받을 금액과 대상을 확인하고 요청 알림을 보낼 수 있습니다.

| 정산 화면 - 받을 때 | 정산 화면 - 보낼 때 |
| - | - |
| ![정산 받기](https://github.com/mymateM/mymate/assets/83498457/712b5c52-cc88-414a-9c18-b2edee3869e6) | ![정산 하기](https://github.com/mymateM/mymate/assets/83498457/b2e70c8e-858d-4cb4-aad1-75ae2fdb80b4) |

| 정산 화면 - 받을 때 (요청 보내기) | 정산 화면 - 보낼 때 (계좌 복사) |
| - | - |
| ![정산 받기(알람)](https://github.com/mymateM/mymate/assets/83498457/aa67a52b-7015-4459-90f8-220296f52535) | ![정산 하기(계좌복사)](https://github.com/mymateM/mymate/assets/83498457/af0c1ddd-752e-4fe5-9733-31cc3ae0d3ff) |

# 개선 목표
* 모듈화 : API를 불러오는 코드, 액세스 코드를 불러오는 과정의 반복이 많아 모듈화할 예정
* 퍼포먼스 증진 : Fragment 대신 Activity로 화면을 구현해 어플리케이션이 무겁고 느린 문제를 개선할 예정
* 기능 구현 : 로그인 / 마이페이지 / 알람 등 일부 기능을 구현 및 개선할 예정
* 최종 목표는 출시입니다.
