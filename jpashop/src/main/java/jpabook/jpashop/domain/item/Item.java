package jpabook.jpashop.domain.item;

import jakarta.persistence.*;
import jpabook.jpashop.domain.Category;
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
}
