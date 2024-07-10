package ru.clevertec.check.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.check.exception.NoSuchProductIdException;
import ru.clevertec.check.exception.ProductNotFoundException;
import ru.clevertec.check.model.Product;


import java.sql.*;



import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JdbcProductRepositoryTest {

    @Mock
    private DatabaseConnection databaseConnectionMock;

    @Mock
    private Connection connectionMock;

    @Mock
    private Statement statementMock;
    @Mock
    private PreparedStatement preparedStatementMock;

    @Mock
    private ResultSet resultSetMock;

    @InjectMocks
    private JdbcProductRepository repository;

    @Test
    public void testGetProductById() throws SQLException, ClassNotFoundException {
        // Arrange
        int productId = 123;

        when(databaseConnectionMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.createStatement()).thenReturn(statementMock);
        when(statementMock.executeQuery(anyString())).thenReturn(resultSetMock);
        when(resultSetMock.next()).thenReturn(true);
        when(resultSetMock.getInt("id")).thenReturn(productId);
        when(resultSetMock.getString("description")).thenReturn("Test Product");
        when(resultSetMock.getDouble("price")).thenReturn(19.99);
        when(resultSetMock.getInt("quantity_in_stock")).thenReturn(50);
        when(resultSetMock.getBoolean("is_wholesale_product")).thenReturn(true);

        // Act
        Product product = repository.getProductById(productId);

        // Assert
        Assertions.assertNotNull(product);


        verify(databaseConnectionMock).getConnection();
        verify(connectionMock).createStatement();
        verify(statementMock).executeQuery(anyString());
        verify(resultSetMock).next();
        verify(resultSetMock).getInt("id");
        verify(resultSetMock).getString("description");
        verify(resultSetMock).getDouble("price");
        verify(resultSetMock).getInt("quantity_in_stock");
        verify(resultSetMock).getBoolean("is_wholesale_product");
        verify(statementMock).close();
    }

    @Test
    void updateProduct_shouldUpdateProductInDatabase() throws SQLException, NoSuchProductIdException, ClassNotFoundException {
        // Arrange
        when(databaseConnectionMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(any(String.class))).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeUpdate()).thenReturn(1);

        Product product = new Product();
        product.setId(1);
        product.setDescription("Test Product");
        product.setPrice(9.99);
        product.setQuantityInStock(100);
        product.setIsWholesaleProduct(true);
        // Act
        repository.updateProduct(product);

        // Assert
        verify(preparedStatementMock, times(1)).setString(1, "Test Product");
        verify(preparedStatementMock, times(1)).setDouble(2, 9.99);
        verify(preparedStatementMock, times(1)).setInt(3, 100);
        verify(preparedStatementMock, times(1)).setBoolean(4, true);
        verify(preparedStatementMock, times(1)).setInt(5, 1);
        verify(preparedStatementMock, times(1)).executeUpdate();

    }

    @Test
    void updateProduct_shouldThrowNoSuchProductIdException_whenNoRowsAreAffected() throws SQLException {
        // Arrange
        when(databaseConnectionMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(any(String.class))).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeUpdate()).thenReturn(0);

        Product product = new Product();
        product.setId(1);
        product.setDescription("Test Product");
        product.setPrice(9.99);
        product.setQuantityInStock(100);
        product.setIsWholesaleProduct(true);

        // Act and Assert
        assertThrows(NoSuchProductIdException.class, () -> repository.updateProduct(product));
    }

    @Test
    public void testDeleteProductById_ValidProduct() throws SQLException, ClassNotFoundException, ProductNotFoundException {
        // Arrange
        int validProductId = 1;
        when(databaseConnectionMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeUpdate()).thenReturn(1);

        // Act
        repository.deleteProductById(validProductId);

        // Assert
        verify(preparedStatementMock).setInt(1, validProductId);
        verify(preparedStatementMock).executeUpdate();
        verify(preparedStatementMock).close();
    }

    @Test
    public void testDeleteProductById_InvalidProduct() throws SQLException, ClassNotFoundException, ProductNotFoundException {
        // Arrange
        int invalidProductId = 99;
        when(databaseConnectionMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeUpdate()).thenReturn(0);


        // Act & Assert
        assertThrows(ProductNotFoundException.class, () -> repository.deleteProductById(invalidProductId));
        verify(preparedStatementMock).setInt(1, invalidProductId);
        verify(preparedStatementMock).executeUpdate();
    }


}
