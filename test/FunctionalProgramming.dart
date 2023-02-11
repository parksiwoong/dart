//set에서 리스트로 바꾸고 Map으로도 바꾸는 형변환 방법
void main(){
  List<String> blackPink =['로제','지수'];
  print(blackPink);

  print(blackPink.asMap()); // {0:로제, 1:지수}
  print(blackPink.toSet()); //{로제,지수}

  Map blackPinkMap = blackPink.asMap();
  print(blackPinkMap.keys);
  print(blackPinkMap.keys.toList());
  print(blackPinkMap.values);
  print(blackPinkMap.values.toList());

  Set blackPinkSet = Set.from(blackPink);
  print(blackPinkSet.toList());
}