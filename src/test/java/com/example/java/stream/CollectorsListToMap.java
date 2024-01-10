package com.example.java.stream;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.java.common.dto.ProductDto;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

/**
 * Collectors.toMap()을 통해 리스트에서 맵으로 변환 List<ProductDto> -> Map<Long, ProductDto>
 */
public class CollectorsListToMap {

    /**
     * List -> Map
     */
    @Test
    void toMap() {
        // given
        final List<ProductDto> productDtoList = getProductDtoList();

        // when
        final Map<Long, ProductDto> productDtoMap = productDtoList.stream()
            .collect(Collectors.toMap(
                    ProductDto::getId, // key (인스턴스 메서드 레퍼런스 사용)
                    Function.identity() // value (입력받은 인스턴스 그대로 리턴)
                )
            );

        // then
        productDtoMap.entrySet().stream()
            .forEach(
                productDto -> System.out.println(
                    String.format("key = %s, value = %s",
                        productDto.getKey(), productDto.getValue().toString())
                )
            );
    }

    /**
     * List -> Map 변환 중 중복 키 발생 (IllegalStateException 발생)
     */
    @Test
    void toMapDuplicated() {
        // given
        final List<ProductDto> productDtoList = getDuplicatedProductDtoList();

        // 키 중복으로 IllegalStateException 발생
        // when & then
        assertThrows(IllegalStateException.class, () -> {
            final Map<Long, ProductDto> productDtoMap = productDtoList.stream()
                .collect(Collectors.toMap(
                        productDto -> productDto.getId(), // key (람다식 사용)
                        productDto -> productDto // (람다식 사용)
                    )
                );
        });
    }

    /**
     * List -> Map 변환 중 중복 키 발생 처리를 위한 mergerFunction 사용
     */
    @Test
    void toMapThroughMerging() {
        // given
        final List<ProductDto> productDtoList = getDuplicatedProductDtoList();

        // when
        final Map<Long, ProductDto> productDtoMap = productDtoList.stream()
            .collect(Collectors.toMap(
                    ProductDto::getId, // key
                    Function.identity(), // value
                    (oldOne, newOne) -> newOne // mergerFunction 사용, 대체 로직을 구현하면 된다 (람다식 사용)
                )
            );

        // then
        productDtoMap.entrySet().stream()
            .forEach(
                productDto -> System.out.println(
                    String.format("key = %s, value = %s",
                        productDto.getKey(), productDto.getValue())
                )
            );
    }

    /**
     * 상품 리스트 생성
     */
    private List<ProductDto> getProductDtoList() {
        return List.of(
            ProductDto.of(1L, "상품1"),
            ProductDto.of(2L, "상품2"),
            ProductDto.of(3L, "상품3")
        );
    }

    /**
     * 중복된 상품 리스트 생성
     */
    private List<ProductDto> getDuplicatedProductDtoList() {
        return List.of(
            ProductDto.of(1L, "상품1old"),
            ProductDto.of(1L, "상품1new"),
            ProductDto.of(3L, "상품3")
        );
    }
}
