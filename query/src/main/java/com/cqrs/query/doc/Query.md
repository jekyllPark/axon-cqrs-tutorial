# Query
## P2P (Point to Point Query)
Query를 처리하는 Handler가 하나만 존재.

한 번만 질의하면 되는 상황이라면 P2P Query가 적합하다.

## Subscription
SSE (Server-Sent-Event)

이를 지원하기 위해 클라이언트에서도 SSE 구현이 필요하며, Connection 유지를 위한 Query가 증가할 수록 그에 상응하는 스레드 수가 증가한다.

## Scatter-Gather Query
동일한 Query를 수행하는 핸들러가 여러 앱에 존재할 경우,

모든 앱에 질의를 하여 결과를 취합하고 최초 질의를 한 앱에 결과를 처리한다.