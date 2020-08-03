package testing;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import chessControllers.Chess;
import chessObjects.Square;

class MoveListTest {

	Chess c = new Chess();
	@Test
	void test() {
		for (int i = 0; i < 16; i++) {
			
		}
		List<Square> list = Chess.getPiece(0, 1).getMoveList();
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i).toString());
		}
	}

}
