package com.shino72.wallet.data

object OttData {
    val ottData get() = _ottData
    private val _ottData : List<Ott> = listOf(
        Ott(name = "netflix", korean = "넷플릭스"),
        Ott(name = "watcha", korean = "왓챠"),
        Ott(name = "coupang", korean = "쿠팡"),
        Ott(name = "disney", korean = "디즈니+"),
        Ott(name = "wavve", korean = "웨이브"),
        Ott(name = "tving", korean = "티빙"),
        Ott(name = "youtube", korean = "유튜브 프리미엄"),
        Ott(name = "amazon", korean = "아마존 프라임"),
        Ott(name = "apple", korean = "애플 티비+"),
        Ott(name = "seezn", korean = "씨즌"),
        Ott(name = "laftel", korean = "라프텔"),
        Ott(name = "millie", korean = "밀리의 서재"),
        Ott(name = "bugs", korean = "벅스"),
        Ott(name = "genie", korean = "지니"),
        Ott(name = "melon", korean = "멜론"),
        Ott(name = "twitch", korean = "트위치 구독"),
        Ott(name = "kakaotalk", korean = "카카오 이모티콘+"),
        Ott(name = "yogipass", korean = "요기패스"),
        Ott(name = "ujupass", korean = "우주패스"),
        Ott(name = "hbomax", korean = "HBO MAX"),
        Ott(name = "naverplus", korean = "네이버 플러스"),
        Ott(name = "nintendo", korean = "닌텐도 스위치"),
        Ott(name = "etc", korean = "기타"),
    )

    fun getOttWithName(name : String) : Ott? {
        return _ottData.find { it.name == name }
    }
}