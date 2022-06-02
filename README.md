# Chess-Engine

Game Setup:
Piece Class(color, tile location)
     King
     Queen
     Pawn
     Knight
     Rook
     Bishop
Board
     Contains an 8 by 8 array of tiles
     List of uncaptured and captured pieces
Tile
     Has a color and either a piece or empty
Move
     Piece on the starting square
     Piece on the ending square
     Starting square and ending square
     Way of determining whether a move is legal or not
Game State
     Board
     Move list(stack – can be used to go back and undo moves, or by the engine in the future)
     Whose turn it is

Display
     DisplayConsole
     DisplayGUI

Engine
