package io.openems.edge.ess.power;

import java.util.Optional;

import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.Relationship;

/**
 * Creates constraints with following settings:
 * <ul>
 * <li>Relationship (EQ, GEQ, LEQ) as given in constructor
 * <li>Value as given in constructor or updated via setValue()
 * <li>Setting each coefficient given at 'indices' to '1', i.e.
 * 
 * <pre>
 * y = 1*p1 + 0*q1 * + 1*p2 + 0*q1 +...
 * </pre>
 * </ul>
 */
public class CoefficientOneConstraint extends AbstractConstraint {

	protected final int[] indices;

	private final Relationship relationship;

	private Optional<Integer> valueOpt;

	public CoefficientOneConstraint(int noOfCoefficients, int[] indices, Relationship relationship, Integer value,
			String note) {
		super(noOfCoefficients, note);
		this.indices = indices;
		this.relationship = relationship;
		this.valueOpt = Optional.ofNullable(value);
	}

	public void setValue(Integer value) {
		this.valueOpt = Optional.ofNullable(value);
	}

	@Override
	public LinearConstraint[] getConstraints() {
		if (this.valueOpt.isPresent()) {
			int value = this.valueOpt.get();
			double[] coefficients = this.initializeCoefficients();
			for (int index : this.indices) {
				coefficients[index] = 1;
			}
			return new LinearConstraint[] { new LinearConstraint(coefficients, this.relationship, value) };
		} else {
			return new LinearConstraint[] {};
		}
	}
}
