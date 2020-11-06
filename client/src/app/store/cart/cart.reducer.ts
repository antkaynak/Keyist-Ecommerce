import * as CartActions from './cart.actions';
import { HttpError } from '../app.reducers';
import { Cart } from '../model';


export interface CartState {
  cart: Cart;
  errors: Array<HttpError>;
  loading: boolean;
  fetchLoading: boolean;
}

const initialState: CartState = {
  cart: null,
  errors: [],
  loading: false,
  fetchLoading: false
};

export function cartReducer(state = initialState, action: CartActions.CartActions) {
  switch (action.type) {
    case (CartActions.SET_CART):
      return {
        cart: action.payload.cart,
        errors: [...state.errors.filter(error => error.errorEffect !== action.payload.effect)],
        loading: false,
        fetchLoading: state.fetchLoading
      };

    case (CartActions.FETCH_CART):
      return {
        ...state,
        fetchLoading: true
      };

    case (CartActions.FETCH_CART_SUCCESS):
      return {
        cart: action.payload.cart,
        errors: [...state.errors.filter(error => error.errorEffect !== action.payload.effect)],
        loading: false,
        fetchLoading: false
      };

    case (CartActions.APPLY_DISCOUNT_SUCCESS):
      return {
        ...state,
        errors: [...state.errors.filter(error => error.errorEffect !== action.payload.effect)],
        loading: false,
      };



    case (CartActions.ADD_TO_CART):
    case (CartActions.REMOVE_FROM_CART):
    case (CartActions.APPLY_DISCOUNT):
    case (CartActions.INCREMENT_CART):
    case (CartActions.DECREMENT_CART):
      return {
        ...state,
        loading: true
      };

    case (CartActions.CART_ERROR):
      const errors = [...state.errors];
      const index = errors.findIndex(error => error.errorEffect === action.payload.errorEffect);
      if (index !== -1) {
        errors[index] = action.payload;
      } else {
        errors.push(action.payload);
      }
      return {
        ...state,
        loading: false,
        errors
      };

    case (CartActions.EMPTY_CART_SUCCESS):
      return initialState;

    default:
      return state;
  }
}
