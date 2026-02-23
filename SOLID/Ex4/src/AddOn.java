public enum AddOn implements AddOnPricing {
    MESS {
        public double price() {
            return 1000.0;
        }
    },
    LAUNDRY {
        public double price() {
            return 500.0;
        }
    },
    GYM {
        public double price() {
            return 300.0;
        }
    };
}
