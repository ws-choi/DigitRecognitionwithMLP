# Digit Recognition with MLP

## 개발자
 - 고려대학교 최우성 (ws_choi@korea.ac.kr)
 
## 프로그램 다운로드
 - [MLP 기반 숫자 인식기.exe](https://github.com/ws-choi/DigitRecognitionwithMLP/raw/master/MLP%20%EA%B8%B0%EB%B0%98%20%EC%88%AB%EC%9E%90%20%EC%9D%B8%EC%8B%9D%EA%B8%B0.exe)
 
## 데이터 (다운로드가 안될 시 우클릭후 다른 이름으로 링크 저장, 또는 내용을 전체 복사하여 메모장에 붙여넣은 후 파일저장)
 - [트레이닝 데이터](https://github.com/ws-choi/DigitRecognitionwithMLP/raw/master/data/Training%20Data.txt)
 - [테스트 데이터](https://github.com/ws-choi/DigitRecognitionwithMLP/raw/master/data/testset.txt)
 
## 사용방법

1. 상단의 Get Training Data File 버튼 클릭으로 다운로드 받은 트레이닝 데이터 선택
2. Data File Parsing Info.에는 파일 업로드시 자동으로 인풋, 아웃풋의 벡터 크기가 표시됩니다.
 - # of input: 100, # of output 10 이라고 나오면 정상
3. Network Layout에는 신경망에 대한 옵션을 조절할 수 있습니다. 
 - 단, 불안정하므로 사용 권장하지 않음
4. Training Options와 Stopping Criteria를 이용하여 트레이닝 조건을 설정 후  Start Training 버튼을 누르면 학습이 시작됩니다.
5. 테스트 인터페이스 완성
 - 숫자를 그려넣고 Test this! 버튼을 누르면 버튼 옆 화면에 인식된 숫자가 출력됩니다. 
6. 일괄 Test
 - 메뉴 바의 [File]=>[test] 에서 테스트 데이터 셋에 대한 일괄 테스트를 할 수 있습니다. 숫자별 테스트 통계가 출력됩니다.
