# HP(Handmade Presentation)

### 작품 정의
손으로 직접 그린 구성도를 딥러닝을 통해 파워포인트로 변환해주는 어플

### 작품 배경
문서 작업을 하면서 불편함을 느껴 생각하게 되었다. 대회에 출품한 작품 보고서를 작성할 때 시스템 구성도 등 도형을 직접 편집해 구성도 같은 그림을 많이 그려야 했다. 
이 구성도들을 파워포인트로 일일이 도형과 글 상자로 편집해 보고서에 넣을 수 있는 이미지로 가져오기까지 많은 시간이 걸렸다.

이러한 불편함을 해소하고자 종이에 손으로 직접 그린 그림을 찍은 이미지를 가지고 딥러닝 알고리즘과 Tesseract를 이용해 파워포인트로 변환해주는 어플리케이션을 생각하게 되었다.

### 구현 파트
서버 구현 및 통합과 텍스트 훈련을 담당

### 시스템 구성도

![시스템구성도](https://user-images.githubusercontent.com/55968079/141241154-0e297172-490b-44a9-ad9d-d9c6451568bf.png)

### YOLO 도형 학습 과정

 - YOLO 알고리즘으로 도형과 텍스트를 인식하는 훈련 진행 
![yolo훈련](https://user-images.githubusercontent.com/55968079/141241246-6242069d-e76f-417d-b93d-82dde5eb98d3.png)

### 손글씨체 훈련 결과
  
  - Tesseract-OCR을 이용해 손글씨체 직접 훈련 후 인식 결과
![글씨훈련](https://user-images.githubusercontent.com/55968079/141241360-bc0a732d-fd13-49c2-b106-1cb2d7317927.PNG)

### 결과
- 어플 수신
![파일수신결과](https://user-images.githubusercontent.com/55968079/141241694-a2d53c76-5e1e-4797-9eb7-432760f1fb2d.PNG)

- 카카오톡과 연동하여 데스크탑에서 수정 작업 가능
![결과](https://user-images.githubusercontent.com/55968079/141241719-31c6239a-6643-42e1-9f1e-bb8733467255.png)


