import * as ShowcaseActions from './showcase.actions';
import { HttpError } from '../app.reducers';
import { Product, ProductVariantResponse } from '../model';


export interface ShowcaseState {
  newlyAdded: Array<Product>;
  mostSelling: Array<ProductVariantResponse>;
  interested: Array<Product>;
  errors: Array<HttpError>;
  loading: Array<string>;
}

const initialState: ShowcaseState = {
  newlyAdded: [],
  mostSelling: [],
  interested: [],
  errors: [],
  loading: []
};

export function showcaseReducer(state = initialState, action: ShowcaseActions.ShowcaseActions) {
  switch (action.type) {
    case (ShowcaseActions.FETCH_NEWLY_ADDED):
      const newlyAddedLoad = [...state.loading];
      newlyAddedLoad.push(ShowcaseActions.FETCH_NEWLY_ADDED);
      return {
        ...state,
        loading: newlyAddedLoad
      };

    case (ShowcaseActions.FETCH_MOST_SELLING):
      const mostSellingLoad = [...state.loading];
      mostSellingLoad.push(ShowcaseActions.FETCH_MOST_SELLING);
      return {
        ...state,
        loading: mostSellingLoad
      };

    case (ShowcaseActions.FETCH_INTERESTED):
      const interestedLoad = [...state.loading];
      interestedLoad.push(ShowcaseActions.FETCH_INTERESTED);
      return {
        ...state,
        loading: interestedLoad
      };

    case (ShowcaseActions.FETCH_NEWLY_ADDED_SUCCESS):
      return {
        ...state,
        newlyAdded: action.payload.res,
        errors: [...state.errors.filter(error => error.errorEffect !== action.payload.effect)],
        loading: [...state.loading.filter(loaded => loaded !== action.payload.effect)]
      };

    case (ShowcaseActions.FETCH_MOST_SELLING_SUCCESS):
      return {
        ...state,
        mostSelling: action.payload.res,
        errors: [...state.errors.filter(error => error.errorEffect !== action.payload.effect)],
        loading: [...state.loading.filter(loaded => loaded !== action.payload.effect)]
      };

    case (ShowcaseActions.FETCH_INTERESTED_SUCCESS):
      return {
        ...state,
        interested: action.payload.res,
        errors: [...state.errors.filter(error => error.errorEffect !== action.payload.effect)],
        loading: [...state.loading.filter(loaded => loaded !== action.payload.effect)]
      };

    case (ShowcaseActions.SHOWCASE_ERROR):
      const errors = [...state.errors];
      const index = errors.findIndex(error => error.errorEffect === action.payload.errorEffect);
      if (index !== -1) {
        errors[index] = action.payload;
      } else {
        errors.push(action.payload);
      }
      return {
        ...state,
        loading: [...state.loading.filter(loaded => loaded !== action.payload.errorEffect)],
        errors
      };

    case (ShowcaseActions.EMPTY_INTERESTED):
      return {
        ...state,
        interested: []
      };

    default:
      return state;
  }
}
