package service;

import com.sun.jdi.InternalException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.check.*;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CheckServiceTest {

    CheckService checkService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private DiscountCardRepository discountCardRepository;


    @Test
    void testCreateCheckWithNullItems() {
        InputParams inputParams = InputParams.builder()
                .discountCard("1234567890")
                .saveToFile("path/to/result.csv")
                .build();

        checkService = new CheckService(inputParams, discountCardRepository, productRepository);
        Assertions.assertThrows(InternalException.class, checkService::createCheck);
    }

    @Test
    void testCreateCheckWithInvalidDiscountCard() {
        InputParams inputParams = InputParams.builder()
                .items(new ArrayList<>())
                .discountCard("abc")
                .saveToFile("path/to/result.csv")
                .build();

        checkService = new CheckService(inputParams, discountCardRepository, productRepository);
        Assertions.assertThrows(InternalException.class, checkService::createCheck);
    }

    @Test
    void testCreateCheckWithNullPathToResult() {
        InputParams inputParams = InputParams.builder()
                .items(new ArrayList<>())
                .discountCard("1234567890")
                .build();

        checkService = new CheckService(inputParams, discountCardRepository, productRepository);
        Assertions.assertThrows(IllegalArgumentException.class, checkService::createCheck);
    }


    @Test
    void testCreateCheckWithInsufficientBalance() {
        InputParams inputParams = InputParams.builder()
                .items(new ArrayList<>())
                .discountCard("1234567890")
                .balanceDebitCard(100.0)
                .saveToFile("path/to/result.csv")
                .build();

        checkService = new CheckService(inputParams, discountCardRepository, productRepository);
        checkService.setTotal(200.0);
        checkService.setDiscount(50.0);

        Assertions.assertThrows(IllegalArgumentException.class, checkService::createCheck);
    }

    @Test
    void testCreateCheckWithValidInput() throws SQLException {

        DiscountCard mockDiscountCard = new DiscountCard();
        mockDiscountCard.setId(1);
        mockDiscountCard.setAmount(3);
        mockDiscountCard.setNumber(1111);
        when(discountCardRepository.getDiscountCardByNumber(anyInt()))
                .thenReturn(mockDiscountCard);
        Product mockProduct = new Product();
        mockProduct.setId(1);
        mockProduct.setDescription("Pineapple 300g");
        mockProduct.setPrice(100);
        mockProduct.setQuantity_in_stock(30);
        mockProduct.setIs_wholesale_product(true);

        when(productRepository.getProductById(anyInt())).thenReturn(mockProduct);

        List<Item> items = new ArrayList<>();
        items.add(new Item(2, 10));
        InputParams inputParams = InputParams.builder()
                .items(items)
                .discountCard("1111")
                .balanceDebitCard(1100.0)
                .saveToFile("./src/main/result.csv")
                .datasourceUsername("abc")
                .datasourcePassword("def")
                .datasourceUrl("database")
                .build();

        checkService = new CheckService(inputParams, discountCardRepository, productRepository);
        checkService.setTotal(150.0);
        checkService.setDiscount(20.0);

        Assertions.assertDoesNotThrow(() -> checkService.createCheck());
    }


}