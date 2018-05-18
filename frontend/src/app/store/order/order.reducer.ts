import * as OrderActions from './order.actions';
import {Discount, ProductDisplay} from "../cart/cart.reducer";
import {HttpError} from "../app.reducers";

export interface OrderDetails {
  id: number,
  productDisplay: ProductDisplay, //expects productDisplay convert it from product in the backed
  amount: number
}

export interface Orders {
  id: number,
  orderDiscount: Discount,
  orderDetailsList: OrderDetails[],
  shipName: string,
  shipAddress: string,
  shipAddress2: string,
  city: string,
  state: string,
  zip: string,
  country: string,
  phone: string,
  totalPrice: number,
  totalCargoPrice: number,
  email: string,
  date: string,
  shipped: number,
  cargoFirm: string,
  trackingNumber: string
}

export interface PaymentObject {
  cardNo: string,
  cardExp: { month: string, year: string }
  cardOwner: string,
  cardCCV: string
}

export interface PostOrdersObject {
  shipName: string,
  shipAddress: string,
  shipAddress2: string,
  city: string,
  state: string,
  zip: string,
  country: string,
  phone: string,
  email: string,
  cargoFirm: string,
}


export interface State {
  postOrders: PostOrdersObject;
  postPayment: PaymentObject;
  isPurchaseActive: boolean;
  errors: HttpError[];

}

const initialState: State = {
  postOrders: null,
  postPayment: null,
  isPurchaseActive: false,
  errors: [] //using array because we can add more reducers with error
};

export function orderReducer(state = initialState, action: OrderActions.OrderActions) { //TODO why are we storing orders again?
  switch (action.type) {
    case(OrderActions.POST_ORDER_FORM):
      let postOrderFormErrorClear = state.errors;
      for (let i = 0; i < postOrderFormErrorClear.length; i++) {
        if (postOrderFormErrorClear[i].errorEffect === 'POST_ORDER_FORM') {
          postOrderFormErrorClear = postOrderFormErrorClear.splice(i, 1);
        }
      }
      return {
        ...state,
        postOrders: action.payload,
        errors: postOrderFormErrorClear
      };
    case(OrderActions.POST_PAYMENT):
      let postPaymentErrorClear = state.errors;
      for (let i = 0; i < postPaymentErrorClear.length; i++) {
        if (postPaymentErrorClear[i].errorEffect === 'POST_PAYMENT') {
          postPaymentErrorClear = postPaymentErrorClear.splice(i, 1);
        }
      }
      return {
        ...state,
        postPayment: action.payload,
        errors: postPaymentErrorClear
      };
    case(OrderActions.IS_PURCHASE_ACTIVE):
      console.log("IS_PURCHASE_ACTIVE");
      console.log(action.payload);
      if (action.payload) {
        return {
          postOrders: null,
          postPayment: null,
          isPurchaseActive: true,
          errors: []
        };
      }
      return {
        postOrders: null,
        postPayment: null,
        isPurchaseActive: false,
        errors: []
      };

    case(OrderActions.ORDER_ERROR):
      let orderErrorPush = state.errors;
      for (let i = 0; i < orderErrorPush.length; i++) {
        if (orderErrorPush[i].errorEffect === action.payload.errorEffect) {
          orderErrorPush[i] = action.payload;
          return {
            ...state,
            errors: orderErrorPush
          };
        }
      }
      orderErrorPush.push(action.payload);
      return {
        ...state,
        errors: orderErrorPush
      };
    case (OrderActions.EMPTY_ORDER):
      return {
        postOrders: null,
        postPayment: null,
        isPurchaseActive: state.isPurchaseActive,
        errors: []
      };
    default:
      return state;
  }
}
