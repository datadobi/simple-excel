/*
 * Copyright (c) 2012-2013, bad robot (london) ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package bad.robot.excel.matchers;

import bad.robot.excel.cell.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import static bad.robot.excel.PoiToExcelCoercions.asExcelCoordinate;
import static bad.robot.excel.matchers.CellType.adaptPoi;

public class CellInRowMatcher extends TypeSafeDiagnosingMatcher<Row> {

	private final Sheet sheet;
    private final Cell expected;
    private final int columnIndex;
    private final String coordinate;

    private CellInRowMatcher(Sheet sheet, org.apache.poi.ss.usermodel.Cell expectedPoi) {
        this.sheet = sheet;
		this.expected = adaptPoi(expectedPoi);
        this.coordinate = asExcelCoordinate(expectedPoi);
        this.columnIndex = expectedPoi.getColumnIndex();
    }

    public static CellInRowMatcher hasSameCell(Sheet sheet, org.apache.poi.ss.usermodel.Cell expected) {
        return new CellInRowMatcher(sheet, expected);
    }

    @Override
    protected boolean matchesSafely(Row row, Description mismatch) {
        Cell actual = adaptPoi(row.getCell(columnIndex));

        if (!expected.equals(actual)) {
            mismatch.appendText("cell at ").appendValue(coordinate).appendText(" contained ").appendValue(actual).appendText(" expected ").appendValue(expected)
            	.appendText(" sheet ").appendValue(sheet.getSheetName());
            return false;
        }
        return true;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("equality of cell ").appendValue(coordinate);
    }
}
