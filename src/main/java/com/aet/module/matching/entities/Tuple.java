package com.aet.module.matching.entities;


/*
 * An extended tuple class
 */
public class Tuple<U, M, D> {

    public final U u1;
    public final M m2;
    public final D d3;

    public Tuple(U u1, M m2, D d3) {
        this.u1 = u1;
        this.m2 = m2;
        this.d3 = d3;
    }

  public static <U, M, D> Tuple<U, M, D> of (U unimpacted, M matched, D dispense) {
        return new Tuple<>(unimpacted, matched, dispense);

  }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((u1 == null) ? 0 : u1.hashCode());
		result = prime * result + ((m2 == null) ? 0 : m2.hashCode());
		result = prime * result + ((d3 == null) ? 0 : d3.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		
		if (getClass() != obj.getClass()) return false;
		
		Tuple<?,?,?> other = (Tuple<?,?,?>) obj;
		
		if (u1 == null) {
			if (other.u1 != null)
				return false;
		} else if (!u1.equals(other.u1))
			return false;
		if (m2 == null) {
			if (other.m2 != null)
				return false;
		} else if (!m2.equals(other.m2))
			return false;
		if (d3 == null) {
			if (other.d3 != null)
				return false;
		} else if (!d3.equals(other.d3))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Tuple [u1=" + u1 + ", m2=" + m2 + ", d3=" + d3 + "]";
	}

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof Tuple)) return false;
//
//        Tuple<?, ?> tuple = (Tuple<?, ?>) o;
//
//        if (u1 != null ? !u1.equals(tuple.u1) : tuple.u1 != null) return false;
//        return m2 != null ? m2.equals(tuple.m2) : tuple.m2 == null;
//    }
//
//    @Override
//    public int hashCode() {
//        int result = u1 != null ? u1.hashCode() : 0;
//        result = 31 * result + (m2 != null ? m2.hashCode() : 0);
//        return result;
//    }
//
//    @Override
//    public String toString() {
//        return "Tuple{" +
//                "u1=" + u1 +
//                ", m2=" + m2 +
//                '}';
//    }
}
