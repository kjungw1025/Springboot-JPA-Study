package jpabook.jpashop.domain;

import jakarta.persistence.*;
import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Category {

    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    @ManyToMany
    // 다대다 관계는 일대다, 다대일로 풀어내야함
    @JoinTable(name = "category_item",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    // 중간 테이블에 있는 category_id와 inverseJoinColumns는 category_item 중간테이블의 Item쪽에 들어가는 item_id
    private List<Item> items = new ArrayList<>();

    // 같은 엔티티에 대해서 셀프로 양방향 연관 관계를 가지게함
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();
}
