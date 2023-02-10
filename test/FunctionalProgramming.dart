
void main(){
  List<String> blackPink =['로제','지수'];
  print(blackPink);

  print(blackPink.asMap()); // {0:로제, 1:지수}
  print(blackPink.toSet()); //{로제,지수}

  Map blackPinkMap = blackPink.asMap();
  print(blackPinkMap.keys);
  print(blackPinkMap.values);
}