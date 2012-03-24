#include<iostream>
#include<fstream>
#include<string.h>
#include<sstream>
#include<vector>
#include<stdlib.h>
#include<map>
#include<math.h>
#include<cmath>
using namespace std;

// A class, which holds x and y position of a node.
class Position {
public:
	int x;
	int y;

	Position() {
	}

	Position(int x, int y) {
		this->x = x;
		this->y = y;
	}
};

// A Node class which keep track of all necessary information
// of relating to the position on the map.
class Node {
public:
	double cost;
	double elevation;
	Position *position;
	int targetId;

	Node() {
	}

	Node(int x, int y, double cost, double elevation, int targetId) {
		this->position = new Position(x, y);
		this->cost = cost;
		this->elevation = elevation;
		this->targetId = targetId;
	}
};

// A minheap queue.
class MinHeap {
private:

	// Contains the heap data, which is a Node.
	vector<Node> data;

	int heapSize;

	int getLeftChildIndex(int nodeIndex) {
		return 2 * nodeIndex + 1;
	}

	int getRightChildIndex(int nodeIndex) {
		return 2 * nodeIndex + 2;
	}

	int getParentIndex(int nodeIndex) {
		return (nodeIndex - 1) / 2;
	}

public:

	MinHeap() {
		heapSize = 0;
	}

	MinHeap(int size) {
		data.resize(size);
		heapSize = 0;
	}

	bool isEmpty() {
		return (heapSize == 0);
	}

	int getHeapSize() {
		return heapSize;
	}

	void siftUp(int nodeIndex);
	void siftDown(int nodeIndex);

	void insert(int x, int y, double cost, double elevation, int targetId);
	void removeMin();

	Node getMin();
};

// After inserting an element, we need to create the min heap again.
// This creates the minheap after an element insert.
void MinHeap::siftUp(int nodeIndex) {
	int parentIndex;
	Node tmp;
	if (nodeIndex != 0) {
		parentIndex = getParentIndex(nodeIndex);
		if (data[parentIndex].cost > data[nodeIndex].cost) {
			tmp = data[parentIndex];
			data[parentIndex] = data[nodeIndex];
			data[nodeIndex] = tmp;
			siftUp(parentIndex);
		}
	}
}

// Adds a node into minheap queue.
void MinHeap::insert(int x, int y, double cost, double elevation, int targetId) {
	heapSize++;
	Node newNode(x, y, cost, elevation, targetId);
	data.push_back(newNode);
	siftUp(heapSize - 1);
}

// After deleting a node, we need to convert the queue back to heap again.
void MinHeap::siftDown(int nodeIndex) {
	int leftChildIndex, rightChildIndex, minIndex;
	Node tmp;
	leftChildIndex = getLeftChildIndex(nodeIndex);
	rightChildIndex = getRightChildIndex(nodeIndex);
	if (rightChildIndex >= heapSize) {
		if (leftChildIndex >= heapSize)
			return;
		else
			minIndex = leftChildIndex;
	} else {
		if (data[leftChildIndex].cost <= data[rightChildIndex].cost)
			minIndex = leftChildIndex;
		else
			minIndex = rightChildIndex;
	}
	if (data[nodeIndex].cost > data[minIndex].cost) {
		tmp = data[minIndex];
		data[minIndex] = data[nodeIndex];
		data[nodeIndex] = tmp;
		siftDown(minIndex);
	}
}

// Removes a node from the queue.
void MinHeap::removeMin() {
	if (isEmpty())
		throw string("Heap is empty");
	else {
		data[0] = data[heapSize - 1];
		data.pop_back();
		heapSize--;
		if (heapSize > 0)
			siftDown(0);
	}
}

// Return the minimum element from the queue.
Node MinHeap::getMin() {
	return data[0];
}

int ROWS; // Number of rows
int COLUMNS; // Number of columns
int NUMBER_OF_TARGETS; // Number of targets
double THRESHOLD; // slope threshold
vector<vector<double> > MAP; // 2D Map, whose value gives the 3d elevation
vector<Position> TARGET; // List of Targets
MinHeap *QUEUE; // MinHeap Queue
map<string, bool> VISITED; // Nodes visited
map<string, int> OPTIMAL_SITE;

string getHashString(int x, int y, int targetId) {
	stringstream out;
	out << x << " " << y << " " << targetId;
	return out.str();
}

void _initializeEnvironment() {
	string lineRead;
	ifstream file;
	char *strX = new char[4];
	char *strY = new char[4];
	char *strElevation = new char[10];

	file.open("example2.txt");
	if (!file) {
		cout << "File cannot be opened";
		exit(0);
	}

	// Read number of rows
	getline(file, lineRead);
	ROWS = atoi(lineRead.c_str());

	// Read number of columns
	getline(file, lineRead);
	COLUMNS = atoi(lineRead.c_str());

	// Read slope threshold
	getline(file, lineRead);
	THRESHOLD = atof(lineRead.c_str());

	// Set the MAP size. MAP[ROWS][COLUMNS]
	MAP.resize(ROWS + 1);
	for (int i = 0; i <= ROWS; i++)
		MAP[i].resize(COLUMNS + 1);

	// Read all the map elevation data.
	for (int i = 1; i <= ROWS; i++) {
		getline(file, lineRead);
		istringstream stringStream;
		stringStream.str(lineRead);
		for (int j = 1; j <= COLUMNS; j++) {
			stringStream >> strElevation;
			MAP[i][j] = atof(strElevation);
		}
	}
	// Loop till the end and read Target positions.
	while (getline(file, lineRead) > 0) {
		istringstream stringStream;
		TARGET.resize(NUMBER_OF_TARGETS + 1);

		stringStream.str(lineRead);

		// Extract X coordinate of target.
		stringStream >> strX;

		// Extract Y coordinate of target.
		stringStream >> strY;

		// Set TARGET to corresponding x & y co-ordinate.
		TARGET[NUMBER_OF_TARGETS].x = atoi(strX);
		TARGET[NUMBER_OF_TARGETS].y = atoi(strY);
		NUMBER_OF_TARGETS++;
	}

	// Initialize the MinHeap queue with the targets.
	QUEUE = new MinHeap();
	for (int i = 0; i < NUMBER_OF_TARGETS; i++) {
		//cout << "Target " << i << " X : " << TARGET[i].x << ", ";
		//cout << "Y : " << TARGET[i].y << "\n";
		QUEUE->insert(TARGET[i].x, TARGET[i].y, 0,
				MAP[TARGET[i].x][TARGET[i].y], i);

		VISITED[getHashString(TARGET[i].x, TARGET[i].y, i)] = true;
	}
	file.close();
}

void _printEnvironment() {
	for (int i = 1; i <= ROWS; i++) {
		for (int j = 1; j <= COLUMNS; j++) {
			cout << MAP[i][j] << " ";
		}
		cout << "\n";
	}
}

bool _chkThresholdAdd(int x, int y, double elevation, int targetId, double cost) {
	// If node is already visited from the source target, do not add
	// to the queue
	if (VISITED[getHashString(x, y, targetId)])
		return false;

	// Check if the position falls below the threshold, then add.
	double h = fabs(elevation - MAP[x][y]);
	h /= sqrt(x^2 + y^2);
	if (h > THRESHOLD)
		return false;
	cout << "Inserting into the queue x = " << x << ", y = " << y
			<< ", cost = " << cost << ", targetId = " << targetId << "\n";
	QUEUE->insert(x, y, cost, MAP[x][y], targetId);
	VISITED[getHashString(x, y, targetId)] = true;
	return true;
}

void _expandNode(Node node) {
	int x = node.position->x;
	int y = node.position->y;
	int targetId = node.targetId;
	double cost = node.cost;

	if (y > 1) {
		cost = node.cost + 1;
		_chkThresholdAdd(x, y - 1, MAP[x][y], targetId, cost);
		cost = node.cost + sqrt(2);
		_chkThresholdAdd(x + 1, y - 1, MAP[x][y], targetId, cost);
	}
	if (x > 1) {
		cost = node.cost + 1;
		_chkThresholdAdd(x - 1, y, MAP[x][y], targetId, cost);
		cost = node.cost + sqrt(2);
		_chkThresholdAdd(x - 1, y + 1, MAP[x][y], targetId, cost);
	}
	if (x > 1 && y > 1) {
		cost = node.cost + sqrt(2);
		_chkThresholdAdd(x - 1, y - 1, MAP[x][y], targetId, cost);
	}
	cost = node.cost + 1;
	_chkThresholdAdd(x, y + 1, MAP[x][y], targetId, cost);
	cost = node.cost + 1;
	_chkThresholdAdd(x + 1, y, MAP[x][y], targetId, cost);
	cost = node.cost + sqrt(2);
	_chkThresholdAdd(x + 1, y + 1, MAP[x][y], targetId, cost);
}

void _getOptimalLanding() {
	Node root;
	while (QUEUE->getHeapSize() != 0) {
		try {
			root = QUEUE->getMin();
			QUEUE->removeMin();
			cout << "Queue size = " << QUEUE->getHeapSize() << "\n";
			OPTIMAL_SITE[getHashString(root.position->x, root.position->y, 0)]++;

			cout << "Expanding the node x = " << root.position->x << ", y = "
					<< root.position->y << " from target " << root.targetId<< "\n";
			cout << "The node occurred " << OPTIMAL_SITE[getHashString(
					root.position->x, root.position->y, 0)] << " times\n";
			if (OPTIMAL_SITE[getHashString(root.position->x, root.position->y,
					0)] == NUMBER_OF_TARGETS)
				break;
			_expandNode(root);
		} catch (string s) {
			cout << "An exception occurred. " << s << endl;
		}
	}
	cout << "Optimal Landing " << root.position->x << " " << root.position->y
			<< "\n";
}

int main() {
	// Initialize the Environment by reading from the input file.
	_initializeEnvironment();
	//_printEnvironment();

	_getOptimalLanding();
	cout << "Program exited successfully\n";
	return 0;
}

