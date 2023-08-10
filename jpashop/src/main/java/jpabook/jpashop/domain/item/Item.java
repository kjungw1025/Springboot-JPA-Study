package jpabook.jpashop.domain.item;

import jakarta.persistence.*;
import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 상속 관계 매핑이기 때문에 전략을 부모 클래스에 지정해야함 (여기서는 한 테이블에 모두 넣으므로 single table)
// Inheritance에는 single_table, table_per_class joined가 있음
@DiscriminatorColumn(name = "dtype") // Album, Book, Movie일 때 각각 어떻게 할 것인지
@Getter @Setter
public abstract class Item {
    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    // Album, Book, Movie -> 상속 관계 매핑

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    // 데이터(price, stockQuantity)를 가지고 있는 쪽의 비즈니스 메서드가 있는게 가장 좋음 -> 응집력 있음
    //==비즈니스 로직==//
    /**
     *  stock 증가
     */
    public void addStock (int quantity) {
        this.stockQuantity += quantity;
    }
    // stockQuantity를 변경할 일이 있으면 @Setter를 사용하지 않고, addStock, removeStock과 같은 비즈니스 메서드를 통해 변경해야 함

    /**
     *  stock 감소
     */
    public void removeStock (int quantity) {
        int restStock = this.stockQuantity -= quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }
}
