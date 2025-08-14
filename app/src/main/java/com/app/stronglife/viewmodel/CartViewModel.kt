import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.app.stronglife.data.model.CartItem
import com.app.stronglife.data.model.Product

open class CartViewModel(
    initialItems: List<CartItem> = emptyList()
) : ViewModel() {
    protected val _cartItems = mutableStateOf(initialItems)
    val cartItems: State<List<CartItem>> = _cartItems

    fun addProduct(product: Product) {
        val current = _cartItems.value.toMutableList()
        val index = current.indexOfFirst { it.product.id == product.id }

        if (index != -1) {
            val updatedItem = current[index].copy(quantity = current[index].quantity + 1)
            current[index] = updatedItem
        } else {
            current.add(CartItem(product, quantity = 1))
        }

        Log.d("CartViewModel", "addProduct: ${product.name}")

        _cartItems.value = current
    }

    fun decreaseProduct(product: Product) {
        val current = _cartItems.value.toMutableList()
        val index = current.indexOfFirst { it.product.id == product.id }

        if (index != -1) {
            val item = current[index]
            if (item.quantity > 1) {
                current[index] = item.copy(quantity = item.quantity - 1)
            } else {
                current.removeAt(index)
            }
            _cartItems.value = current
        }
    }

    fun removeProduct(product: Product) {
        val current = _cartItems.value.toMutableList()
        current.removeAll { it.product.id == product.id }
        _cartItems.value = current
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }
}
