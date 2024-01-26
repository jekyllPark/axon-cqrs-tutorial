# TRIES
## 대시보드랑 프레임워크 의존성 버전 안 맞아서 커넥션 안 되는 이슈 있어 한참 삽질
- 릴리즈 노트 찾아서 호환되는 버전 찾아서 해결.. 힘들었다...
## xStream forbiddenClassException
단순 yml 설정으로 해결이 안되어 xStream 빈으로 별도로 설정파일 넣어줌.
```
    @Bean
    public XStream xStream() {
        XStream xStream = new XStream();
        xStream.allowTypes(new Class[]{
                HolderCreationEvent.class,
                AccountCreationEvent.class,
                DepositMoneyEvent.class,
                WithdrawMoneyEvent.class
        });
        return xStream;
    }
```
## JDK21 TimeUnit 임폴트 안 되는 이슈
https://youtrack.jetbrains.com/issue/KT-62389/JDK-21-Cannot-access-class-TimeUnit.-Check-your-module-classpath-for-missing-or-conflicting-dependencies

위 링크 참고, 해결 되지 않음. 인텔리제이 버전 및 JDK 다운그레이드 시도해보기