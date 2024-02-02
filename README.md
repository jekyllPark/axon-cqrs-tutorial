# Axon Framework
# Command
## Aggregate
### 예제
```

  @Aggregate
  @Entity(name = "account")
  @Table(name = "account")
  public class AccountAggregate {
  
      @AggregateIdentifier
      @Id
      @Column(name = "account_id")
      private String accountId;
      
      @AggregateMember
      @ManyToOne(fetch = FetchType.LAZY)
      @JoinColumn(name = "holder_id", foreignKey = @ForeignKey(name = "FK_HOLDER"))
      private HolderAggregate holder;
      
      private Long balance;
  
      @CommandHandler
      public AccountAggregate(AccountCreationCommand command) {
          this.accountId = command.getAccountId();
          HolderAggregate holder = command.getHolder();
          this.balance = 0L;
          apply(new AccountCreationEvent(holder.getHolderId(), command.getAccountId()));
      }
  
      @EventSourcingHandler
      protected void createAccount(AccountCreationEvent event) {
          this.accountId = event.getAccountId();
          this.holderId = event.getHolderId();
          this.balance = 0L;
      }
      
      protected Account() {}
  }
```
- ```@AggregateIdentifier```
   - 외부 참초지점으로 이 필드를 통해 커맨드의 대상이 되는 에그리거트를 판단한다. (필수 값)
- ```@CommandHandler```
  - 요청 커맨드를 처리할 대상임을 나타내는 어노테이션으로, 생성자(에그리거트 생성) 및 비즈니스 로직에 추가된다.
- ```AggregateLifecycle#apply```
  - 적용 대상이 되는 이벤트에 메시지를 발행한다.
- ```@EventSourcingHandler```
  - 위 어노테이션을 통해 에그리거트가 발행하는 이벤트를 수신할 수 있다.
  - ```AggregateIdentifier```는 에그리거트가 발행한 첫 이벤트의 ```@EventSourcingHandler```에 설정되어야 한다.
    - 쉽게 생각하면 이벤트 객체에 집계 식별자가 꼭 포함되어야 한다는 말.
- 에그리거트는 과거 이벤트를 사용하여 초기화하기 전  ```빈 생성자```를 통해 인스턴스가 생성 되어야 한다. 그렇지 않을 경우, 예외가 발생한다.
- ```@AggregateMember```
  - 해당 어노테이션이 달린 필드의 메시지 핸들러를 검사해야 하는 대상임을 프레임워크에 알려준다.
- 상위 에그리거트에서 하위 에그리거트를 생성할 때 주의점
  - ```AggregateLifecycle#createNew``` 를 통해 생성할 수 있다. 
  - 이벤트 소싱 핸들러가 아닌 커맨드 핸들러를 통해 에그리거트를 생성해야 된다. (이벤트가 소싱될 때 에그리거트를 생성하면 의도치 않은 결과가 나올 수 있음.)
  - 하위 애그리거트는 생성되었음을 명시적으로 나타내주기 위한 이벤트를 발행해야 한다. (그렇지 않을 경우, 상위 에그리거트 커맨드 처리에 포함된다.)
  - https://docs.axoniq.io/reference-guide/axon-framework/axon-framework-commands/modeling/aggregate-creation-from-another-aggregate
## 다형성
```
  public abstract class Card {}
  
  public class GiftCard extends Card {}
  
  public class ClosedLoopGiftCard extends GiftCard {}
  
  public class OpenLoopGiftCard extends GiftCard {}
  
  public class RechargeableGiftCard extends ClosedLoopGiftCard {}
```
> axoniq example

- 다형성 계층을 유지하기 위해 다음의 제약 조건을 염두해야 한다.
  - 추상 클래스에 생성자를 갖는 것을 허용하지 않는다.
  - 핸들러는 파생될 수 없기에 계층 구조 간 서로 다른 에그리거트에 동일 커맨드를 통해 생성자를 생성해선 안 된다.
  - ```@AggregateIdentifier``` 에그리거트 식별자를 각 계층 간 여러 필드에 선언할 수 없다.

- 다형성 에그리거트 계층은 ```AggregateConfigurer```에서 ```AggregateConfigurer#withSubtypes(Set<Class<? extends A>>)``` 를 통해 등록할 수 있다.
```
public class AxonConfig {
    // omitting other configuration methods...
    public AggregateConfigurer<GiftCard> giftCardConfigurer() {
        Set<Class<? extends GiftCard>> subtypes = new HashSet<>();
        subtypes.add(OpenLoopGiftCard.class);
        subtypes.add(RechargeableGiftCard.class);

        return AggregateConfigurer.defaultConfiguration(GiftCard.class)
                                  .withSubtypes(subtypes);
    }

    // ...
}
```
> axoniq 코드

## Command Dispatchers
Axon은 커맨드 핸들러에 명령을 보내기 위해 두 가지 인터페이스를 제공한다.
1. CommandBus
   - 커맨드 메시지를 전달하는 기능을 제공
   - ```dispatch(CommandMessage)```, ```dispatch(CommandMessage, CommandCallback)```
   - ```The GenericCommandMessage#asCommandMessage(Object)``` 를 통해 커맨드 메시지 생성을 할 수 있다.
   - ```CommandBus#dispatch(CommandMessage)``` 를 통해 커맨드 메시지를 커맨드 버스에 디스패치하여 커맨드 핸들러로 전달한다.
2. CommandGateway
  - ```send``` 와 ```sendAndWait``` 메소드가 있는데, send는 비동기, sendAndWait는 동기 방식이다.
  - 명령 메시지를 전달하는 기능을 제공하는 인터페이스이며, 내부적으로 CommandBus 인터페이스를 통해 메시지를 디스패치한다.

## Command Handlers
- 커맨드 핸들러는 컴포넌트에 둘 수도 있지만 상태가 포함된 에그리거트에서 직접 정의하는 것이 좋다.
- 커맨드에 대상이 되는 객체의 에그리거트 식별자는 커맨드의 필드에 ```@TargetAggregateIdentifier``` 어노테이션이 붙어야 한다.
```
-- 에그리거트
@Aggregate
public class Account {
  @AggregateIdentifier
  @Id
  private Long accountId;
  
  @CommandHandler
  public Account (AccountCreationCommand command) {
    ...로직
  }
  
  .... 기타 생략
}

-- 커맨드
public class AccountCreationCommand {
  @TargetAggregateIdentifier  // 에그리거트의 식별자가 타겟임을 선언해줘야 함
  private Long accountId;
  .. 기타 필드
}

```
> 예시

- 에그리거트의 상태 변경은 반드시 이벤트 소싱 핸들러를 통해 업데이트 되어야 한다.
  - 만약 커맨드 핸들러를 통해 처리될 경우 상태 변경에 대한 이력을 놓칠 수 있다.


# 이벤트
## Event Dispatchers
이벤트 발행은 에그리거트 내 혹은 컴포넌트에서 주로 이루어진다.

에그리거트 내 이벤트가 전달되는 케이스는 위 예제에서 살펴보듯 ```AggregateLifecycle#apply```를 통한 것이다.

```
  @CommandHandler
  public AccountAggregate(AccountCreationCommand command) {
      // .. 기타 로직
      apply(new AccountCreationEvent(holder.getHolderId(), command.getAccountId())); // 이 부분
  }
```

```AggregateLifecycle#apply``` 는 아래와 같은 단계를 거쳐 이벤트를 발행하게 된다.

1. 에그리거트의 현재 범위를 검색한다.
2. 마지막으로 알려진 시퀀스 번호를 활용하여 게시할 이벤트의 시퀀스 번호를 설정한다.
3. 제공된 페이로드는(파라미터) 이벤트 메시지로 래핑되며 이전 단계의 시퀀스 번호와 에그리거트의 식별자를 수신한다.
4. 이벤트 핸들러를 통해 이벤트를 처리하고, 에그리거트의 상태를 업데이트하기 위해 이벤트 소싱 작업이 필요하다. (```@EventSourcingHandler``)
5. 위 일련의 과정을 거친 후 이벤트 버스를 통해 이벤트를 게시한다.

- 대부분 에그리거트를 통해 이벤트를 발행하겠지만 때때로 직접 이벤트를 발행해야 할 때가 있다면? eventGateway를 활용한다.
```
private EventGateway eventGateway;

public void dispatchEvent() {
    eventGateway.publish(new AccountCreationEvent("holderId", "accountId"));
}
```

# ref
- 모든 자료는 axoniq 공식 문서로부터 참고하여 작성했습니다.
- https://docs.axoniq.io/reference-guide/axon-framework/axon-framework-commands/modeling/aggregate