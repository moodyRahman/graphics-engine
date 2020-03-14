package src;


class DoubleMatrixDimensionError extends RuntimeException {
        private static final long serialVersionUID = 1L;

        /**
         * DoubleMatrix Dimensions Error
         * 
         * @param error error message
         */
        public DoubleMatrixDimensionError(String error) {
                super(error);
        }
}

class ParameterError extends RuntimeException{
        private static final long serialVersionUID = 1L;
        public ParameterError(String error, int line){
                super();
        }
}