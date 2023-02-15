Const APIKey As String = "API키" '<- API 키를 입력합니다.
Const APIUrl As String = "https://api.openai.com/v1/completions"

'---------------------------- ▼▼ 사용할 명령문을 이 안에 작성하세요 ▼▼ -------------------------------
'■xGPT_Run 함수
'xGPT_Run "입력셀주소", "출력셀주소", [List출력여부(True/False)]
'예) xGPT_Run "C6", "B8"

Sub MyGPT_Test1()



End Sub

Sub MyGPT_Test2()



End Sub

'---------------------------- ▲▲ 사용할 명령문을 이 안에 작성하세요 ▲▲ -------------------------------

Function xGPT(sRequest, Optional Temperature As Double = 0, Optional Max_Tokens As Integer = 0)

'■ GPT 모델 설명은 아래 링크를 참고하세요.
'https://platform.openai.com/docs/models/gpt-3
'■ 비용에 대한 설명은 아래 링크를 참고하세요.
'https://openai.com/api/pricing/
'■ 비용 요약
'1000토큰 = 한글 450~500자 or 영어 750단어 or A4용지 절반 분량
'davinci는 최대 4000토큰 지원 ≒ 약 A2장 분량 입/출력 가능
'davinci 모델(가장 좋은 성능) : 1000토큰 당 24원 (A4용지 한장 ≒ 50원)
'curie 모델(보편적 성능, 가성비 좋음) : 1000토큰당 2.4원 (A4용지 한장 ≒ 5원)

Application.EnableEvents = False

If sRequest = "" Then xGPT = "": Exit Function

Dim GPTModel As String: GPTModel = "text-davinci-003" '"text-davinci-003" '또는 "text-curie-001"
If Max_Tokens = 0 Then
If GPTModel = "text-curie-001" Then Max_Tokens = 1024
If GPTModel = "text-davinci-003" Then Max_Tokens = 2000
End If
Dim vHeader As Variant: ReDim vHeader(0 To 1)
vHeader(0) = Array("Content-Type", "application/json")
vHeader(1) = Array("Authorization", "Bearer " & APIKey)
Dim bodyJSON As String
Dim sResult As String

sRequest = Replace(Replace(Replace(Replace(sRequest, Chr(10), "\n"), Chr(13), "\n"), Chr(9), "\t"), """", "\""")
bodyJSON = "{"
bodyJSON = bodyJSON & """model"" : """ & GPTModel & """, "
bodyJSON = bodyJSON & """prompt"" : """ & sRequest & """, "
bodyJSON = bodyJSON & """temperature"" : " & Temperature & ", "
bodyJSON = bodyJSON & """max_tokens"" : " & Max_Tokens
bodyJSON = bodyJSON & "}"

sResult = GetHttp(APIUrl, bodyJSON, RequestHeader:=vHeader, RequestType:="POST").Body.innerHTML

If InStr(1, sResult, """error"":") = 0 Then

sResult = Splitter(sResult, "[{""text"":""", """,""")

Do While Left(sResult, 2) = "\n"
sResult = Right(sResult, Len(sResult) - 2)
Loop

sResult = Replace(Replace(sResult, "\n", vbNewLine), "\""", """")

xGPT = sResult
Else

sResult = Splitter(sResult, """message"": """, """,")
xGPT = "#Error! : " & sResult

End If

Application.EnableEvents = True

End Function

Sub xGPT_Run(sPromptRange As String, sPrintRange As String, Optional isList As Boolean = True, Optional Temparature As Double = 0, Optional Max_Tokens As Integer = 2500)

Dim WS As Worksheet: Set WS = ActiveSheet
Dim PromptRange As Range: Set PromptRange = WS.Range(sPromptRange)
Dim PrintRange As Range: Set PrintRange = WS.Range(sPrintRange)
Dim vArrray As Variant
Dim ArrCount As Long

If isList = True Then
vArray = xGPT_List(PromptRange.Value, Temparature, Max_Tokens)
If PrintRange.Value <> "" Then PrintRange.CurrentRegion.ClearContents
PrintRange.Resize(UBound(vArray), 1) = vArray
Else
PrintRange.Value = xGPT(PromptRange.Value, Temparature, Max_Tokens)
End If

MsgBox "요청하신 작업이 완료되었습니다."

End Sub

Function xGPT_List(sRequest, Optional Temperature As Double = 0, Optional Max_Tokens As Integer = 2500)

Dim sResult As String
Dim vResult As Variant: Dim vReturn As Variant
Dim i As Long

sResult = xGPT(sRequest, Temperature)
vResult = Split(sResult, vbNewLine)

ReDim vReturn(1 To UBound(vResult) + 1, 1 To 1)

For i = LBound(vResult) To UBound(vResult)
vReturn(i + 1, 1) = Trim(vResult(i))
Next

xGPT_List = vReturn

End Function

Function GetHttp(URL As String, Optional formText As String, _
Optional isWinHttp As Boolean = False, _
Optional RequestHeader As Variant, _
Optional includeMeta As Boolean = False, _
Optional RequestType As String = "GET") As Object

'###############################################################
'오빠두엑셀 VBA 사용자지정함수 (https://www.oppadu.com)
'▶ GetHttp 함수
'▶ 웹에서 데이터를 받아옵니다.
'▶ 인수 설명
'_____________URL : 데이터를 스크랩할 웹 페이지 주소입니다.
'_____________formText : Encoding 된 FormText 형식으로 보내야 할 경우, Send String에 쿼리문을 추가합니다.
'_____________isWinHttp : WinHTTP 로 요청할지 여부입니다. Redirect가 필요할 경우 True로 입력하여 WinHttp 요청을 전송합니다.
'_____________RequestHeader : RequestHeader를 배열로 입력합니다. 반드시 짝수(한 쌍씩 이루어진) 개수로 입력되어야 합니다.
'_____________includeMeta : TRUE 일 경우 HTML 문서위로 ResponseText를 강제 입력합니다. Meta값이 포함되어 HTML이 작성되며 innerText를 사용할 수 없습니다. 기본값은 False 입니다.
'_____________RequestType : 요청방식입니다. 기본값은 "GET"입니다.
'▶ 사용 예제
'Dim HtmlResult As Object
'Set htmlResult = GetHttp("https://www.naver.com")
'msgbox htmlResult.body.innerHTML
'###############################################################

Dim oHTMLDoc As Object: Dim objHTTP As Object
Dim HTMLDoc As Object
Dim i As Long: Dim blnAgent As Boolean: blnAgent = False
Dim sUserAgent As String: sUserAgent = "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.183 Mobile Safari/537.36"

Application.DisplayAlerts = False

If Left(URL, 4) <> "http" Then URL = "http://" & URL

Set oHTMLDoc = CreateObject("HtmlFile")
Set HTMLDoc = CreateObject("HtmlFile")

If isWinHttp = False Then
Set objHTTP = CreateObject("MSXML2.ServerXMLHTTP")
Else
Set objHTTP = CreateObject("WinHttp.WinHttpRequest.5.1")
End If

objHTTP.setTimeouts 1200000, 1200000, 1200000, 1200000 '응답 대기시간 120초
objHTTP.Open RequestType, URL, False
If Not IsMissing(RequestHeader) Then
Dim vRequestHeader As Variant
For Each vRequestHeader In RequestHeader
Dim uHeader As Long: Dim Lheader As Long: Dim steps As Long
uHeader = UBound(vRequestHeader): Lheader = LBound(vRequestHeader)
If (uHeader - Lheader) Mod 2 = 0 Then GetHttp = CVErr(xlValue): Exit Function
For i = Lheader To uHeader Step 2
If vRequestHeader(i) = "User-Agent" Then blnAgent = True
objHTTP.setRequestHeader vRequestHeader(i), vRequestHeader(i + 1)
Next
Next
End If
If blnAgent = False Then objHTTP.setRequestHeader "User-Agent", sUserAgent

objHTTP.send formText

If includeMeta = False Then
With oHTMLDoc
.Open
.Write objHTTP.responseText
.Close
End With
Else
oHTMLDoc.Body.innerHTML = objHTTP.responseText
End If

Set GetHttp = oHTMLDoc
Set oHTMLDoc = Nothing
Set objHTTP = Nothing

Application.DisplayAlerts = True

End Function

Function Splitter(v As Variant, Cutter As String, Optional Trimmer As String)

'###############################################################
'오빠두엑셀 VBA 사용자지정함수 (https://www.oppadu.com)
'▶ Splitter 함수
'▶ Cutter ~ Timmer 사이의 문자를 추출합니다. (Timmer가 빈칸일 경우 Cutter 이후 문자열을 추출합니다.)
'▶ 인수 설명
'_____________v : 문자열입니다.
'_________Cutter : 문자열 절삭을 시작할 텍스트입니다.
'_________Trimmer : 문자열 절삭을 종료할 텍스트입니다. (선택인수)
'▶ 사용 예제
'Dim s As String
's = "{sa;b132@drama#weekend;aabbcc"
's = Splitter(s, "@", "#")
'msgbox s '--> "drama"를 반환합니다.
'###############################################################

Dim vaArr As Variant

On Error GoTo EH:

vaArr = Split(v, Cutter)(1)
If Not IsMissing(Trimmer) Then vaArr = Split(vaArr, Trimmer)(0)

Splitter = vaArr

Exit Function

EH:
Splitter = ""

End Function

Function GoogleTranslate(OriginalText, _
Optional sFrom As String = "auto", _
Optional sTo As String = "") As String

'■변수 선언 및 할당
Dim strURL As String: Dim objHTTP As Object
Dim objHTML As Object: Dim objDivs As Object
Dim objDiv As Variant: Dim strResult As String
Dim vaRng As Variant: Dim Rng As Variant

If sTo = "" Then
If Application.LanguageSettings.LanguageID(msoLanguageIDUI) = 1042 Then sTo = "ko" Else sTo = "en"
End If

If TypeName(OriginalText) = "Range" Then
For Each vaRng In OriginalText
For Each Rng In vaRng
If Rng <> "" Then
strtext = strtext & Rng & vbNewLine
Else
strtext = strtext & vbNewLine
End If
Next
Next
Else
strtext = OriginalText
End If

'■ 구글 번역 API 요청
strtext = ENCODEURL(strtext)
strURL = "https://translate.google.com/m?hl=" & sFrom & _
"&sl=" & sFrom & _
"&tl=" & sTo & _
"&ie=UTF-8&prev=_m&q=" & strtext

On Error GoTo EH:
Set objHTTP = CreateObject("MSXML2.ServerXMLHTTP")
objHTTP.Open "GET", strURL, False
objHTTP.setRequestHeader "User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.183 Mobile Safari/537.36"
objHTTP.send ""

Set objHTML = CreateObject("htmlfile")
With objHTML
.Open
.Write objHTTP.responseText
.Close
End With

'■ 번역 결과 확인
If InStr(1, objHTTP.responseText, "Error 413") > 0 Then GoogleTranslate = "#Request Too Large!": Exit Function
Set objDivs = objHTML.getElementsByTagName("div")
For Each objDiv In objDivs
If objDiv.className = "result-container" Then
strResult = objDiv.innerHTML: Exit For
End If
Next objDiv

'■ 결과값 출력
GoogleTranslate = Convert_Html_Entities(strResult)

Set objHTML = Nothing: Set objHTTP = Nothing
Exit Function

EH:
GoogleTranslate = "#TimeOut"
Set objHTML = Nothing: Set objHTTP = Nothing
End Function

Function Convert_Html_Entities(c)

c = Replace(c, "&quot;", """")
c = Replace(c, "&amp;", "&")
c = Replace(c, "&apos;", "'")
c = Replace(c, "&lt;", "<")
c = Replace(c, "&gt;", ">")
c = Replace(c, "&nbsp;", vbNewLine)
c = Replace(c, "&iexcl;", "¡")
c = Replace(c, "&cent;", "￠")
c = Replace(c, "&pound;", "￡")
c = Replace(c, "&curren;", "¤")
c = Replace(c, "&yen;", "￥")
c = Replace(c, "&brvbar;", "|")
c = Replace(c, "&sect;", "§")
c = Replace(c, "&uml;", "¨")
c = Replace(c, "&copy;", "ⓒ")
c = Replace(c, "&ordf;", "ª")
c = Replace(c, "&laquo;", "≪")
c = Replace(c, "&not;", "￢")
c = Replace(c, "&reg;", "®")
c = Replace(c, "&deg;", "°")
c = Replace(c, "&plusmn;", "±")
c = Replace(c, "&sup2;", "²")
c = Replace(c, "&sup3;", "³")
c = Replace(c, "&acute;", "´")
c = Replace(c, "&micro;", "μ")
c = Replace(c, "&para;", "¶")
c = Replace(c, "&middot;", "·")
c = Replace(c, "&cedil;", "¸")
c = Replace(c, "&sup1;", "¹")
c = Replace(c, "&ordm;", "º")
c = Replace(c, "&raquo;", "≫")
c = Replace(c, "&frac14;", "¼")
c = Replace(c, "&frac12;", "½")
c = Replace(c, "&frac34;", "¾")
c = Replace(c, "&iquest;", "¿")
c = Replace(c, "&times;", "×")
c = Replace(c, "&szlig;", "ß")
c = Replace(c, "&divide;", "÷")
c = Replace(c, "&yuml;", "y")
c = Replace(c, "&circ;", "^")
c = Replace(c, "&tilde;", "~")
c = Replace(c, "&mdash;", "-")
c = Replace(c, "&lsquo;", "'")
c = Replace(c, "&rsquo;", "'")
c = Replace(c, "&sbquo;", "'")
c = Replace(c, "&ldquo;", """")
c = Replace(c, "&rdquo;", """")
c = Replace(c, "&bdquo;", """")
c = Replace(c, "&dagger;", "†")
c = Replace(c, "&Dagger;", "‡")
c = Replace(c, "&hellip;", "…")
c = Replace(c, "&permil;", "‰")
c = Replace(c, "&lsaquo;", "?")
c = Replace(c, "&rsaquo;", "?")
c = Replace(c, "&euro;", "€")
c = Replace(c, "&trade;", "™")
c = Replace(c, "&rsquo;", "’")
c = Replace(c, "&laquo;", "≪")
c = Replace(c, "&raquo;", "≫")
c = Replace(c, "&cent;", "￠")
c = Replace(c, "&copy;", "ⓒ")
c = Replace(c, "&micro;", "μ")
c = Replace(c, "&pound;", "￡")
c = Replace(c, "&yen;", "￥")
c = Replace(c, "&yuml;", "y")
c = Replace(c, "&acute;", "´")

Convert_Html_Entities = c

End Function

Function ENCODEURL(varText As Variant, Optional blnEncode = True)

'###############################################################
'오빠두엑셀 VBA 사용자지정함수 (https://www.oppadu.com)
'▶ EncodeURL 함수
'▶ 한글/영문, 특수기호가 포함된 문자열을 웹 URL 표준 주소로 변환합니다.
'▶ 인수 설명
'_____________varTest : 표준 URL 주소로 변환할 문자열입니다.
'_____________blnEncode : TRUE 일 경우 결과값을 출력합니다.
'▶ 사용 예제
's = "http://www.google.com/search=사과"
's = ENCODEURL(s)
'MsgBox s
'###############################################################

Static objHtmlfile As Object

If objHtmlfile Is Nothing Then
Set objHtmlfile = CreateObject("htmlfile")
With objHtmlfile.parentWindow
.execScript "function encode(s) {return encodeURIComponent(s)}", "jscript"
End With
End If

If blnEncode Then
ENCODEURL = objHtmlfile.parentWindow.encode(varText)
End If

End Function


//
        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