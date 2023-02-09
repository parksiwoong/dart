import 'package:dart/dart.dart' as dart;


/* if문 for문 switch 등 비슷한건 대부분 넣지않음 (거의 자바랑 동일) */
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


double number = 4.0;
number ??= 3.0;

int number1 = 1;
int number2 = 2;
print(number1 > number2); // false

/*************************************************************************************************/
/** 타입 비교 */
  int number3 = 1;
print(number3 is int); //인트인지 비교 true
print(number3 is String); //인트인지 비교 false
print(number3 is! int); //인트인지 비교 false
print(number3 is! String); //인트인지 비교 true

/*************************************************************************************************/
// list
List<String> pink= ['제니','로제','리사'];

//map
Map<String,String> dictionary = { 'Harry Poterr' : '해리포터'};

//맵에 추가 2가지
dictionary.addAll({ 'Spiderman' : '스파이더맨'});
dictionary['Hulk'] = '헐크';


}

/*************************************************************************************************/
//Enum 사용법
enum Status{
  approved,
  pending
}

void main1(){
  Status status = Status.approved;

  if(status == Status.approved){
    print('승인입니다.');
  }
}

/*************************************************************************************************/
// optional (옵셔널) : 있어도 되고 없어도 되는 파라메터
// 인트를 선언했지만 안넣으면 되는데 ? 를 넣으면 Null로 해줌
void main3(){
  addNumbers(10);
}
  // [int y = 20 , int z =30] 디폴트값지정도 가능함 (z를 사용하지않으면 null이 아닌 30으로 시작함)
addNumbers(int x, [int? y, int? z]){
  int sum = x + y + z ; // ? 일때는 null + null 이기 떄문에 에러뜸
}

// named parameter - 이름이 있는 파라메터지만 순서가 상관없는 매개변수
void main4(){
  addNumbers1(x: 10, z: 30, y: 20);
}
addNumbers1({
required int x,
required int y,
required int z // int z =30  < 디폴트값으로 넣어도 됨 , (아무것도 넣지않으면 30으로 시작함) >
}){
  print('x : ${x}');
}


//위에껄 람다식으로 표현
void main5(){
  addNumbers1(x: 10, z: 30, y: 20);
}
addNumbers2({
  required int x,
  required int y,
  required int z // int z =30 < 디폴트값으로 넣어도 됨 , (아무것도 넣지않으면 30으로 시작함) >
})=> print('x : ${x}');

/*************************************************************************************************/
// Typedef 함수랑 비슷하지만 함수에 바디가 없는거
void main6(){
 Opertion opertion = add;
 int result1 = opertion(10,20,30);
 print(result1);
}
typedef Opertion = double Function(int x, int y, int z);

int add(int x, int y ,int z) => x + y + z;
int subtract(int x, int y ,int d) => x - y - d; //타입이 맞으면 됨


//Typedef 좀 더 심화
void main7(){
  int result3 = caculate1(10, 20, 30, add2);
  print(result3);
}
typedef Opertion2 = double Function(int x, int y, int z);

int add2(int x, int y ,int z) => x + y + z;
int caculate1(int x, int y ,int z, Opertion2 opertion2) {
  opertion2(x , y ,z);
}

/*************************************************************************************************/
/**PART.2  - OOP */

void main8(){
  IDol blackPink = IDol( //인스턴스
    '블랙핑크',
    ['지수', '제니']
  );
  print(blackPink.name);

  IDol bts = IDol.fromList(['RM', '진']);

  print(blackPink.firstMember); //getter  값: 지수   String get firstMember{ ..
  print(bts.firstMember); //setter 값: RM
}
class IDol{
  String name='블랙핑크';
  List<String> members = ['지수', '제니'];
  //생성자 생성법
  IDol(String name, List<String> members)
    : this.name = name, //this가 뜻하는건 클래스의 name members , {}대신 : 로 쓸 수 있음
      this.members = members;

  IDol.fromList(List values)
    : this.members = values[0],
      this.name = values[1];

  void sayHello(){ print('안녕하세요 블랙핑크입니다.');
  }
/*************************************************************************************************/
//getter , setter 사용방법

  //getter
  String get firstMember{
    return this.members[0];
  }

  //set
  set firstMember(String name){
      this.members[0] = name;
  }

  /**/
}

/*************************************************************************************************/

/** 언더스코어 (프라이비트)  _ 는 private 라고 생각하면됨 컴파일하더라도 _Idols은 생성자로 가져오지않으면 읽히지 않음 */

void main9(){
  _Idols idols = _Idols( //이렇게 가져와서 써야함
    '블랙핑크'
  );
}
class _Idols{
  final String name;

  _Idols(this.name);
  void intraduce(){
    print('저희맴버는 ${this.name} 입니다. ');
  }
}

/*************************************************************************************************/
/** 상속 */
void main10(){
  print('----- Idol ------');
  Idol apink = Idol(name: '에이핑크', membersCount: 5);

  apink.sayMembersCount();
  apink.sayName();

  BoyGroup bts = BoyGroup('bts', 7);
  bts.sayMembersCount();
  bts.name;
}
class Idol{
  String name;
  int membersCount;

  Idol({
   required this.name,
   required this.membersCount,
  });

  void sayName(){
    print('저는 ${this.name} 입니다. ');
  }
  void sayMembersCount(){
    print('${this.name}은 ${this.membersCount}명의 맴버가 있습니다.');
  }
}
// super 는 부모클래스의 변수를 지칭하는것
class BoyGroup extends Idol{
  // BoyGroup({required super.name, required super.membersCount}); 상속 두가지 방법중 1
  BoyGroup(String name, int membersCount,): super(name : name, membersCount: membersCount);

}
/*************************************************************************************************/
/** 메소드 오버라이딩 *
 * override - 덮어쓰다 (우선시하다)
 *
 */
void main11(){
  TimesTwo tt = TimesTwo(2);
  TimesFour tf = TimesFour(2);
}
class TimesTwo {
  final int numberTwo;

  TimesTwo(
      this.numberTwo,
      );
  int calculate(){
    return numberTwo * 2;
  }
}
class TimesFour extends TimesTwo{
 // TimesFour(super.numberTwo);
  TimesFour(int numberTwo): super(numberTwo);

  @override
  int calculate(){
    return super.numberTwo * 4; //2*4 = 8
  }
  //부모클레스의 카큘레이터 실행하는방법은
/*  @override
  int calculate(){
    return super.calculate() * 2;  // 2*2*2 = 8
  }*/
}
/*************************************************************************************************/
// 인스턴스 구속
void main12(){
 Employee seulgi = Employee('슬기');
 //Employee chorong = Employee('초롱'); Final 일땐 한번 선언해서 초롱을 적으면 변경을 못함
  seulgi.name = '코드펙토리';
  Employee.building = '오토타워';
  seulgi.printNameAndBuilding();
}
class Employee{
  //string 이긴하나 널값이 들어가도 되는거
  static String? building;
   //final String name;
   String name;

   Employee(this.name);

   void printNameAndBuilding(){
    print('제이름은 $name 입니다. $building 건물에서 근무하고 있습니다. ');
  }
}

/*************************************************************************************************/
//인터페이스
void main13(){

}
class IdolInterface{
String name;
IdolInterface(this.name);
void sayName(){}
}
class BoyGroup implements IdolInterface{
  String name;
  BoyGroup(this.name);
  void sayname(){}
}