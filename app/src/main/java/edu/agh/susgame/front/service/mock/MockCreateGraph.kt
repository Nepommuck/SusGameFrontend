package edu.agh.susgame.front.service.mock

//fun createCustomMapState(): GameManager {
//
//    val nodes = listOf(
//        Router(NodeId(0), "R1", Coordinates(150, 150), mutableIntStateOf(30)),
//        Router(NodeId(1), "R2", Coordinates(250, 150), mutableIntStateOf(30)),
//        Router(NodeId(2), "R3", Coordinates(200, 25), mutableIntStateOf(30)),
//
//        Host(NodeId(3), "H1", Coordinates(100, 275), PlayerId(0)),
//        Host(NodeId(4), "H2", Coordinates(200, 275), PlayerId(1)),
//        Host(NodeId(5), "H3", Coordinates(350, 250), PlayerId(2)),
//
//        Server(NodeId(6), "S1", Coordinates(75, 50), 300),
//    )
//    val serverId = NodeId(6)
//
//    val edges = listOf(
//        Edge(EdgeId(0), NodeId(3), NodeId(0), 5),
//        Edge(EdgeId(1), NodeId(4), NodeId(0), 5),
//        Edge(EdgeId(2), NodeId(1), NodeId(5), 5),
//        Edge(EdgeId(3), NodeId(0), NodeId(1), 5),
//        Edge(EdgeId(4), NodeId(0), NodeId(2), 5),
//        Edge(EdgeId(5), NodeId(0), NodeId(6), 5),
//        Edge(EdgeId(6), NodeId(1), NodeId(2), 5),
//        Edge(EdgeId(7), NodeId(2), NodeId(6), 5),
//    )
//
//    val players = listOf(
//        PlayerREST(
//            nickname = PlayerNickname("Player_0"),
//            colorHex = 0xAF00FF00,
//            id = PlayerId(0)
//        ),
//        PlayerREST(
//            nickname = PlayerNickname("Player_1"),
//            colorHex = 0xAFF000FF,
//            id = PlayerId(1)
//        ),
//        PlayerREST(
//            nickname = PlayerNickname("Player_2"),
//            colorHex = 0xAFFF5733,
//            id = PlayerId(2)
//        ),
//    )
//    val mapSize = Coordinates(1000, 1000)
//
////    val gameManagerState = GameManager(
////        nodesList = nodes,
////        edgesList = edges.toMutableStateList(),
////        playersList = players,
////        serverId = serverId,
////        mapSize = mapSize,
////        localPlayerId = PlayerId(0)
////    )
//
//    return gameManagerState
//}