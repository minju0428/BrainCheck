# 🧠 MBTI 퍼센테이지: 뇌피셜 검증소 (Brain Check)

책, 드라마, 영화 속 등장인물의 MBTI를 사용자가 예측하고, AI가 제시한 분석 결과와 다를 경우 작품 속 근거와 논리를 들어 AI를 설득하여 '설득률 (%)'을 획득하는 대결 프로그램.

---

## 📘 프로젝트 개요

MBTI 퍼센테이지: 뇌피셜 검증소 (Brain Check)**는 책·드라마·영화 속 등장인물의 행동과 가치관을 기반으로 **MBTI를 예측하고**, AI가 제시한 분석과 비교하여 **설득 대결(Persuasion Battle)**을 펼치는 지적 게임형 웹 애플리케이션

### 🎯 주제 선정 이유

일반적인 콘텐츠 감상은 줄거리 위주로 이루어져, 등장인물의 **심리·가치관·내면 동기**를 깊이 이해하기 어렵습니다.

Brain Check는 사용자가 작품 속 행동 근거를 바탕으로 **캐릭터의 MBTI를 능동적으로 분석**하고, AI와 논리적 대화를 통해 작품을 더 깊게 이해하도록 돕습니다.

### 🎯 프로젝트 목표

* AI를 상대로 자신의 **캐릭터 해석력(캐해력)**을 검증
* 높은 **설득률(%)** 달성으로 성취감 제공
* 작품 속 등장인물에 대한 **입체적 통찰** 습득
* 비판적 사고 및 논리적 사고력 강화

---

## 🏗️ 전체 시스템 구조

Brain Check는 **Controller → Service → DTO** 구조의 Layered Architecture로 설계되었습니다.

---

## 📦 DTO (Data Transfer Object) 계층

DTO는 각 화면(단계)에서 필요한 데이터를 구조화하여 전달하는 역할을 합니다.

### **1. CharacterFormDto**

* 사용자 최초 입력(캐릭터 이름, 작품명, 카테고리)
* 필수 입력값 유효성 검사 포함

### **2. PredictionDto**

* AI 분석 결과(MBTI 퍼센트)
* 사용자가 직접 선택한 MBTI
* 다음 단계로 데이터를 안전하게 전달하기 위한 통합 구조

### **3. AiAnalysisDto**

* AI 분석 결과 + 사용자 MBTI + 일치/불일치 여부
* 분석 결과 페이지에서 사용하는 정적 데이터 포함

### **4. PersuasionBattleDto**

가장 큰 데이터 구조로, 설득 대결 진행에 필요한 모든 상태를 포함합니다.

* 현재 라운드
* 설득률
* 현재 진행하는 MBTI 차원
* 히스토리: 사용자 논거, AI 피드백, 점수 기록
* 종료 여부 플래그

---

## 🧩 Controller 계층

요청을 받아 Service 계층에 전달하고, 적절한 View로 연결하는 역할을 담당합니다.

### **1. CharacterController**

* 등장인물 정보를 입력받아 AI 검증 요청
* 성공 시 분석된 MBTI 데이터를 Prediction 단계로 전달

### **2. PredictionController**

* 사용자 MBTI 선택 + AI 분석 데이터를 결합
* AiAnalysis 단계로 데이터 전달 (데이터 브리지 역할)

### **3. AiAnalysisController**

* AI MBTI vs 사용자 MBTI **불일치 여부 판정**
* 불일치 차원 리스트 구성
* Persuasion Battle 초기 상태 설정

### **4. PersuasionBattleController**

* 대결 시작(`/start`), 라운드 제출(`/submit`), 결과(`/final`)
* 라운드 진행 시: AI 피드백 및 점수 업데이트
* 모든 차원 완료 시 최종 결과 페이지로 이동

---

## 🔧 Service 계층

AI 연동과 핵심 비즈니스 로직을 처리하는 핵심 영역입니다.

### **1. AiPromptService**

* Google Vertex AI SDK 기반 Gemini 모델과 통신
* 등장인물 존재 여부 검증
* 캐릭터의 MBTI 및 퍼센트 분석
* 설득 라운드에서 사용자 논거 평가(Score + Feedback)

### **2. AiAnalysisService**

* AI MBTI 퍼센트 → E/I, S/N, T/F, J/P 차원별 구조화
* 사용자 MBTI와 불일치 차원 목록 생성
* Persuasion Battle 초기 상태 구성

### **3. PersuasionBattleRoundService**

* 사용자 입력 논거 → AI 점수 반영
* 설득률 누적 및 히스토리 기록
* 3라운드 종료
* 모든 차원 완료 시 대결 종료 처리

---

## 🎮 브랜치별 내용

### 1️⃣ **홈 (Home)**

* 서비스 소개 및 시작 버튼 제공

### 2️⃣ **캐릭터 정보 입력 (Input)**

* 인물 이름, 작품명, 카테고리 입력
* 입력 예시 카드 제공

### 3️⃣ **MBTI 예측 (Prediction)**

* 사용자가 직접 MBTI 4차원 선택
* 실시간 미리보기 제공

### 4️⃣ **AI 분석 결과 (AI Analysis)**

* AI의 MBTI 퍼센트 및 근거 제공
* 사용자 MBTI와 비교하여 일치 여부 표시
* 불일치 시 설득 대결로 이동

### 5️⃣ **설득 대결 (Persuasion Battle)**

* 사용자 논거 입력 → AI 반박 제공
* 설득률 실시간 시각화
* 3라운드 제한 또는 100% 달성 규칙 적용

### 6️⃣ **최종 결과 (Final Result)**

* 최종 설득률 및 등급 제공
* 라운드별 점수 및 통계 요약
* 강점·개선점 피드백 제공
* 재도전 가능

---

## 💡 기대 효과

* 작품 이해력 및 캐릭터 분석 능력 향상
* 논리적 사고와 근거 제시 능력 강화
* AI 기반 인터랙션 경험 제공

---

