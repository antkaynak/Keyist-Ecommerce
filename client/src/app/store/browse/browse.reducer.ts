import * as BrowseActions from './browse.actions';
import { HttpError } from '../app.reducers';
import { Category, Colors, ProductVariantResponse } from '../model';


export interface BrowseState {
  products: Array<ProductVariantResponse>;
  productsCount: number;
  categories: Array<Category>;
  colors: Array<Colors>;
  canFetch: boolean;
  selectedPage: number;
  selectedSort: string;
  selectedCategory: string;
  selectedColor: string;
  minPrice: string;
  maxPrice: string;
  errors: Array<HttpError>;
  loading: boolean;
}

const initialState: BrowseState = {
  products: [],
  productsCount: 0,
  colors: null,
  categories: [],
  canFetch: true,
  selectedPage: 0,
  selectedSort: 'any',
  selectedCategory: 'any',
  selectedColor: 'any',
  minPrice: '0',
  maxPrice: '0',
  errors: [],
  loading: false
};

export function browseReducer(state = initialState, action: BrowseActions.BrowseActions) {
  switch (action.type) {
    case (BrowseActions.FETCH_PRODUCTS_APPEND):
    case (BrowseActions.FETCH_PRODUCTS):
      return {
        ...state,
        loading: true
      };

    case (BrowseActions.FETCH_PRODUCTS_SUCCESS):
      return {
        ...state,
        selectedPage: action.payload.selectedPage,
        selectedSort: action.payload.selectedSort,
        selectedCategory: action.payload.selectedCategory,
        selectedColor: action.payload.selectedColor,
        minPrice: action.payload.minPrice,
        maxPrice: action.payload.maxPrice,
        products: action.payload.res,
        canFetch: action.payload.res.length !== 0,
        errors: [...state.errors.filter(error => error.errorEffect !== action.payload.effect)],
        loading: false
      };

    case (BrowseActions.FETCH_PRODUCTS_APPEND_SUCCESS):
      return {
        ...state,
        selectedPage: action.payload.selectedPage,
        selectedSort: action.payload.selectedSort,
        selectedCategory: action.payload.selectedCategory,
        selectedColor: action.payload.selectedColor,
        minPrice: action.payload.minPrice,
        maxPrice: action.payload.maxPrice,
        products: [...state.products, ...action.payload.res],
        canFetch: action.payload.res.length !== 0,
        errors: [...state.errors.filter(error => error.errorEffect !== action.payload.effect)],
        loading: false
      };

    case (BrowseActions.FETCH_PRODUCTS_COUNT_SUCCESS):
      return {
        ...state,
        productsCount: action.payload.res,
        errors: [...state.errors.filter(error => error.errorEffect !== action.payload.effect)]
      };

    case (BrowseActions.FETCH_CATEGORY_SUCCESS):
      return {
        ...state,
        categories: action.payload.res,
        errors: [...state.errors.filter(error => error.errorEffect !== action.payload.effect)]
      };

    case (BrowseActions.FETCH_COLORS_SUCCESS):
      return {
        ...state,
        colors: action.payload.res,
        errors: [...state.errors.filter(error => error.errorEffect !== action.payload.effect)]
      };

    case (BrowseActions.BROWSE_ERROR):
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

    default:
      return state;
  }
}
