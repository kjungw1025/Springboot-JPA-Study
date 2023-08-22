package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    // Dirty Checking (변경 감지) 기능 사용
    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity) {
        Item findItem = itemRepository.findOne(itemId);
        // id를 기반으로 실제 DB에 있는 영속 상태의 entity를 찾았음

        /**
         * 아래 처럼 그저 setter를 사용하는 거보다 메서드를 만드는 것이 추후 에러 발생 시 추적하기 용이함
         * ex. findItem.change(name, price, stockQuantity);
         */
        findItem.setName(name);
        findItem.setPrice(price);
        findItem.setStockQuantity(stockQuantity);

//        itemRepository.save(findItem);
//        entity manager의 persist 호출을 해줄 필요없음
/**
 * findItem으로 가져온 item은 영속 상태임. 이후 값을 세팅한 이후에 Transactional에 의해 자동으로 커밋됨
 * 커밋이 되면 JPA는 flush를 날려 (영속성 컨텍스트들 중에서 변경 된 것들을 모두 찾음) 바뀐 값들을 Update 쿼리를 db에 날려서 update 시킴
 */
    }


    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }

}