import {ProductDisplay} from "../cart/cart.reducer";
import * as ShowcaseActions from "./showcase.actions";
import {HttpError} from "../app.reducers";


export interface State {
  newlyAdded: ProductDisplay[];
  mostSelling: ProductDisplay[];
  interested: ProductDisplay[];
  errors: HttpError[];
}

const initialState: State = {
  newlyAdded: [],
  mostSelling: [],
  interested: [],
  errors: []
};

export function showcaseReducer(state = initialState, action: ShowcaseActions.ShowcaseActions) {
  switch (action.type) {
    case (ShowcaseActions.FETCH_NEWLY_ADDED_SUCCESS):
      let fetchNewlyAddedErrorClear = state.errors;
      for (let i = 0; i < fetchNewlyAddedErrorClear.length; i++) {
        if (fetchNewlyAddedErrorClear[i].errorEffect === 'FETCH_NEWLY_ADDED') {
          fetchNewlyAddedErrorClear = fetchNewlyAddedErrorClear.splice(i, 1);
        }
      }
      return {
        ...state,
        newlyAdded: action.payload,
        errors: fetchNewlyAddedErrorClear
      };
    case (ShowcaseActions.FETCH_MOST_SELLING_SUCCESS):
      let fetchMostSellingErrorClear = state.errors;
      for (let i = 0; i < fetchMostSellingErrorClear.length; i++) {
        if (fetchMostSellingErrorClear[i].errorEffect === 'FETCH_MOST_SELLING') { //TODO put retry button on the field if it fails
          fetchMostSellingErrorClear = fetchMostSellingErrorClear.splice(i, 1);
        }
      }
      return {
        ...state,
        mostSelling: action.payload,
        errors: fetchMostSellingErrorClear
      };
    case (ShowcaseActions.FETCH_INTERESTED_SUCCESS):
      let fetchInterestedErrorClear = state.errors;
      for (let i = 0; i < fetchInterestedErrorClear.length; i++) {
        if (fetchInterestedErrorClear[i].errorEffect === 'FETCH_INTERESTED') {
          fetchInterestedErrorClear = fetchInterestedErrorClear.splice(i, 1);
        }
      }
      return {
        ...state,
        interested: action.payload,
        errors: fetchInterestedErrorClear
      };
    case(ShowcaseActions.SHOWCASE_ERROR):
      let showcaseErrorPush = state.errors;
      for (let i = 0; i < showcaseErrorPush.length; i++) {
        if (showcaseErrorPush[i].errorEffect === action.payload.errorEffect) {
          showcaseErrorPush[i] = action.payload;
          return {
            ...state,
            errors: showcaseErrorPush
          };
        }
      }
      showcaseErrorPush.push(action.payload);
      return {
        ...state,
        errors: showcaseErrorPush
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
