import * as CartActions from './cart.actions';
import {HttpError} from "../app.reducers";

export interface Product {
  cartDesc: string,
  id: number,
  image: string,
  live: number,
  longDesc: string,
  name: string,
  price: number,
  cargoPrice: number,
  taxPercent: number,
  productCategory: { name: string, id: number },
  sku: string,
  stock: number,
  unlimited: number
}

export interface ProductDisplay {
  id: number,
  name: string,
  price: number,
  cargoPrice: number,
  productCategory: { name: string, id: number },
  thumb: string
}

export interface Discount {
  id: number;
  code: string;
  discountPercent: number;
}

export interface CartItem {
  id: number;
  cartProduct: ProductDisplay;
  amount: number;
}

export interface Cart {
  id: number;
  cartItemList: any[];
  cartDiscount: Discount;
  totalPrice: number;
  totalCargoPrice: number;
}


export interface State {
  cart: Cart;
  errors: HttpError[];
  loading: boolean;
  fetchLoading: boolean;
}

const initialState: State = {
  cart: {
    id: null,
    cartItemList: [],
    cartDiscount: null,
    totalPrice: 0,
    totalCargoPrice: 0,
  },
  errors: [],
  loading: false,
  fetchLoading: false // cart fetch loading is different because it exists solely on the header
};

export function cartReducer(state = initialState, action: CartActions.CartActions) {
  switch (action.type) {
    case(CartActions.FETCH_CART):
      return {
        ...state,
        fetchLoading: true
      };

    case (CartActions.FETCH_CART_SUCCESS):
      let fetchErrorClear = state.errors;
      for (let i = 0; i < fetchErrorClear.length; i++) {
        if (fetchErrorClear[i].errorEffect === action.payload.effect) {
          fetchErrorClear = fetchErrorClear.splice(i, 1);
        }
      }
      if (action.payload.cart == null || action.payload.cart == undefined) {
        return {
          cart: {
            id: null,
            cartItemList: [],
            cartDiscount: null,
            totalPrice: 0,
            totalCargoPrice: 0,
          },
          errors: fetchErrorClear,
          loading: state.loading,
          fetchLoading: false
        }
      }
      return {
        cart: action.payload.cart,
        errors: fetchErrorClear,
        loading: state.loading,
        fetchLoading: false
      };

    case(CartActions.ADD_TO_CART):
    case(CartActions.REMOVE_FROM_CART):
    case(CartActions.APPLY_DISCOUNT):
      return {
        ...state,
        loading: true
      };

    case (CartActions.SET_CART):
      let cartErrorClear = state.errors;
      for (let i = 0; i < cartErrorClear.length; i++) {
        if (cartErrorClear[i].errorEffect === action.payload.effect) {
          cartErrorClear = cartErrorClear.splice(i, 1);
        }
      }
      if (action.payload.cart == null || action.payload.cart == undefined) {
        return {
          cart: {
            id: null,
            cartItemList: [],
            cartDiscount: null,
            totalPrice: 0,
            totalCargoPrice: 0,
          },
          errors: cartErrorClear,
          loading: false,
          fetchLoading: state.fetchLoading
        }
      }
      return {
        cart: action.payload.cart,
        errors: cartErrorClear,
        loading: false,
        fetchLoading: state.fetchLoading
      };
    case(CartActions.CART_ERROR):
      let cartErrorPush = state.errors;
      for (let i = 0; i < cartErrorPush.length; i++) {
        if (cartErrorPush[i].errorEffect === action.payload.errorEffect) {
          cartErrorPush[i] = action.payload;
          return {
            ...state,
            errors: cartErrorPush,
            loading: false
          };
        }
      }
      cartErrorPush.push(action.payload);
      return {
        ...state,
        errors: cartErrorPush,
        loading: false
      };

    case(CartActions.EMPTY_CART_SUCCESS):
      return initialState;

    default:
      return state;
  }
}
