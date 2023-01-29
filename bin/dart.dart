import 'package:dart/dart.dart' as dart;

void main(List<String> arguments) {
  print('Hello world: ${dart.calculate()}!');

  /**
   * < var , String, int, double >     등등 선언 방법
   * */

  String name = '코드펙토리';
  String name2 = '코드펙토리';
  //var 는 오른쪽 값을 유추해서 하는 변수
  print(name2.runtimeType);

  //스트링안에다가 변수 넣기
  print('${name} 을 넣을 수 있다.');

/*************************************************************************************************/
  /** < dynamic >
   * dynamic : var변수와 다르게 String이던 name3 이 Int값으로 바꿀 수 도 있다.
   *
      nullable - 널이 될 수 있다.  // 타입뒤 ? 를 넣어준다
      not-nullable - 널이 될 수 없다.  nameNULL! !를 넣어준다.
      null - 아무런 값도 있지않다.
   */

  dynamic name3 = '코트팩토리';
  name3 = 1;


  String? nameNUll = '블랙핑크'; // 타입과 널이 들어 갈 수 있는 경우
  nameNUll = null;
  print(name2!); // 현재 이 값은 널이 아니다.

/*************************************************************************************************/
  /** < final const >
   * final , const : 한번 선언 후 값을 변경 할 수없다.
   *                  final name5 ='' 타입도 생략 가능
   * 차이 : const  빌드타임의 값을 알고 있어야하지만 final은 몰라도 됨
   * 빌드타임이란 ? 01010 컴퓨터만 읽는 글을 사람이 읽을 수 있게 변경해주는 순간을 빌드타임이라 함
   *    그래서 const는 작성하는 순간 지금당장 값을 알고 있어야하는데 DateTime이 읽어지면서 코드가 빌드가 될때 값을 가져오는거라 에러남
   * */

  final String name5 = '코드팩토리';
  const String name6 = '코드팩토리';

  //현재시간 이지만 지금 이 변수가 실행될때 그 순간의 시간을 재는거
  DateTime now = DateTime.now();
  //const DateTime now1 = DateTime.now(); 콘스트로 하면 에러 남, 빌드타임의 값을 알고 있어야하지만 final은 몰라도 됨

/*************************************************************************************************/
/**
 * 비교하는 오퍼레이터
* ?? 는 변수의 값이 널이면 오른쪽값이 들어갈것이고 변수값이 널이 아니면 넣지않는다는 뜻 */
int num = 1;
//print(num++);
//print(++num);

double number = 4.0;
number ??= 3.0;

int number1 = 1;
int number2 = 2;
print(number1 > number2); // false

  
}
