package dataAS1;

import java.util.Iterator;
import java.util.NoSuchElementException;

// KEY --> Integer index, VALUE --> String kelime
public class HashedDictionary<V, K> {
	private TableEntry<V, K>[] hashTable;
	private double numberOfEntries;
	private double locationsUsed;
	private int freqCounter = 0;
	private int coll = 0;
	private double load_factor = 0;
	SingleLinkedList freqs = new SingleLinkedList();
	private static final int DEFAULT_SIZE = 2477;
	private static final double MAX_LOAD_FACTOR = 0.5;
	private int hashType = -1;
	private int collType = -1;
	public int rehashCount = 0;

	public HashedDictionary() {
		this(DEFAULT_SIZE);
	}

	public HashedDictionary(int tableSize) {
		int primeSize = getNextPrime(tableSize);
		hashTable = new TableEntry[primeSize];
		numberOfEntries = 0;
		locationsUsed = 0;
	}

	public boolean isPrime(int num) {
		boolean prime = true;
		for (int i = 2; i <= num / 2; i++) {
			if ((num % i) == 0) {
				prime = false;
				break;
			}
		}
		return prime;
	}

	public int getNextPrime(int num) {
		if (num <= 1)
			return 2;
		else if (isPrime(num))
			return num;
		boolean found = false;
		while (!found) {
			num++;
			if (isPrime(num))
				found = true;
		}
		return num;
	}

	public int getHashIndex(K key) {
		Hash hasher = new Hash();
		hasher.strToHash = (String) key;
		int hashIndex = 0;
		if (this.getHashType() == 0) {
			hashIndex = hasher.ssf() % hashTable.length;
		} else if (this.getHashType() == 1) {
			hashIndex = hasher.paf() % hashTable.length;
		}

		if (hashIndex < 0) {
			hashIndex = hashIndex + hashTable.length;
		}
		return hashIndex;
	}

	public boolean isHashTableTooFull() {
		load_factor = locationsUsed / hashTable.length;
		if (load_factor >= MAX_LOAD_FACTOR)
			return true;
		return false;
	}

	public void add(V value, K key, Object fileName) {
		// Value = 232 Key = Car Object = txt1
		if (isHashTableTooFull()) {
			rehash();
		}

		int index = getHashIndex(key);

		if (this.getCollType() == 0) {
			index = lineerProbe(index, key);
		} else if (this.getCollType() == 1) {
			index = doubleHash(index, key);
		}
		if ((hashTable[index] == null) || hashTable[index].isRemoved()) {
			SingleLinkedList sll = new SingleLinkedList();
			hashTable[index] = new TableEntry<V, K>(value, key);
			Node newnode = new Node(fileName, 1);
			hashTable[index].freqCounter = 1;
			hashTable[index].fileName = fileName;
			sll.add(newnode);
			hashTable[index].freqSLL = sll;
			numberOfEntries++;
			locationsUsed++;
		} else {
			SingleLinkedList sll = hashTable[index].getSLL();
			if (hashTable[index].fileName.equals(fileName)) {
				int newFreq = hashTable[index].getFreq() + 1;
				hashTable[index].setFreq(newFreq);
				sll.searchDataAndFreq(fileName, newFreq);
				hashTable[index].freqSLL = sll;
			} else {
				hashTable[index].freqCounter = 1;
				hashTable[index].fileName = fileName;
				sll.searchDataAndFreq(fileName, 1);
				hashTable[index].freqSLL = sll;
			}

			hashTable[index].setKey(key);
		}

	}

	public void rehash() {
		TableEntry<V, K>[] oldTable = hashTable;
		int oldSize = hashTable.length;
		int newSize = getNextPrime(2 * oldSize);
		hashTable = new TableEntry[newSize];
		numberOfEntries = 0;
		locationsUsed = 0;

		for (int index = 0; index < oldSize; index++) {
			if (oldTable[index] != null && oldTable[index].isRemoved() == false) {
				K oldKey = oldTable[index].getKey();
				V oldValue = oldTable[index].getValue();
				SingleLinkedList oldSLL = oldTable[index].getSLL();
				Node oldHead = oldSLL.head;
				int idx = getHashIndex(oldKey);
				if (this.getCollType() == 0) {
					idx = lineerProbe(idx, oldKey);
				} else if (this.getCollType() == 1) {
					idx = doubleHash(idx, oldKey);
				}

				if ((hashTable[idx] == null) || hashTable[idx].isRemoved()) {
					hashTable[idx] = new TableEntry<V, K>(oldValue, oldKey);
					SingleLinkedList sll = new SingleLinkedList();
					int count = 0;
					if (oldSLL.size() == 1) {
						hashTable[idx].fileName = oldHead.getFileName();
						hashTable[idx].freqCounter = oldHead.getFreq();
						Node newnode = new Node(oldHead.getFileName(), hashTable[idx].freqCounter);
						sll.add(newnode);
					} else {
						while (oldHead != null) {
							hashTable[idx].fileName = oldHead.getFileName();
							hashTable[idx].freqCounter = oldHead.getFreq();
							Node newnode = new Node(oldHead.getFileName(), hashTable[idx].freqCounter);
							if (count == 0) {
								sll.add(newnode);
							} else {
								sll.searchDataAndFreq(oldHead.getFileName(), freqCounter);
							}
							count++;
							if (oldHead.getLink() != null) {
								oldHead = oldHead.getLink();
							} else {
								break;
							}

						}
					}
					hashTable[idx].freqSLL = sll;
					numberOfEntries++;
					locationsUsed++;

				} else {
					hashTable[idx].setKey(oldKey);
				}
			}

		}

	}

	public int lineerProbe(int index, K key) {
		boolean found = false;
		int removedStateIndex = -1;
		while (!found && (hashTable[index] != null)) {
			if (hashTable[index].isIn()) {
				if (key.equals(hashTable[index].getKey())) {
					found = true;
				} else {
					index = (index + 1) % hashTable.length;
				}
			} else {
				if (removedStateIndex == -1) {
					removedStateIndex = index;
				}
				index = (index + 1) % hashTable.length;
			}

		}
		if (found || (removedStateIndex == -1)) {
			return index;
		} else {
			return removedStateIndex;
		}
	}

	public int doubleHash(int index, K key) {
		// index = key'in hash değeri
		int h1 = index;
		boolean found = false;
		int h2 = 31 - (h1 % 31);
		int count = 1;
		int removedStateIndex = -1;
		while (!found && (hashTable[index] != null)) {
			if (hashTable[index].isIn()) {
				if (key.equals(hashTable[index].getKey())) {
					found = true;
				} else {
					index = (index + h2) % hashTable.length;
				}
			} else {
				if (removedStateIndex == -1) {
					removedStateIndex = index;
				}
				// index = (index + h2) % hashTable.length;
			}

		}
		if (found || (removedStateIndex == -1)) {
			return index;
		} else {
			return removedStateIndex;
		}

	}

	public void remove(K key) {
		int index = getHashIndex(key);
		index = locate(index, key);

		if (index != -1) {
			hashTable[index].setToRemoved();
			numberOfEntries--;
		}
	}

	public SingleLinkedList search(K key) {

		boolean found = false;
		SingleLinkedList tempSLL = new SingleLinkedList();
		int index = getHashIndex(key);
		int hashFunc1 = getHashIndex(key);
		int h2 = 31 - (hashFunc1 % 31);
		while (!found && hashTable[index] != null) {
			if (hashTable[index].isIn() && key.equals(hashTable[index].getKey())) {
				found = true;
				tempSLL = hashTable[index].freqSLL;
				break;

			} else {
				if (this.getCollType() == 0) {
					index = (index + 1) % hashTable.length;
					coll++;
				} else if (this.getCollType() == 1) {
					coll++;
					index = (index + h2) % hashTable.length;

				}

			}
		}

		return tempSLL;
	}

	public Object[][] searchThreeWords(K[] keys) {
		K firstKey = keys[0];
		K secondKey = keys[1];
		K thirdKey = keys[2];
		Object[][] resultArr = new Object[70][2];
		int entryCount = 0;
		SingleLinkedList firstSLL = new SingleLinkedList();
		firstSLL = search(firstKey);
		SingleLinkedList secondSLL = new SingleLinkedList();
		secondSLL = search(secondKey);
		SingleLinkedList thirdSLL = new SingleLinkedList();
		thirdSLL = search(thirdKey);

		Node head1 = firstSLL.head;
		Node head2 = secondSLL.head;
		Node head3 = thirdSLL.head;

		for (int i = 0; i < firstSLL.size(); i++) {

			resultArr[entryCount][0] = head1.getFileName();
			resultArr[entryCount][1] = head1.getFreq();
			entryCount++;
			if (head1.getLink() != null) {
				head1 = head1.getLink();
			} else {
				break;
			}
		}

		for (int i = 0; i < secondSLL.size(); i++) {
			Object fileName = head2.getFileName();
			int freq = head2.getFreq();
			boolean found = false;
			for (int j = 0; j < entryCount; j++) {
				if (resultArr[j][0].equals(fileName)) {
					int currentIndexFreq = (Integer) resultArr[j][1];
					currentIndexFreq += freq;
					resultArr[j][1] = currentIndexFreq;
					found = true;
				}
			}
			if (!found) {
				resultArr[entryCount][0] = fileName;
				resultArr[entryCount][1] = freq;
				entryCount++;
			}
			if (head2.getLink() != null) {
				head2 = head2.getLink();
			} else {
				break;
			}

		}

		for (int i = 0; i < thirdSLL.size(); i++) {
			Object fileName = head3.getFileName();
			int freq = head3.getFreq();
			boolean found = false;
			for (int j = 0; j < entryCount; j++) {
				if (resultArr[j][0].equals(fileName)) {
					int currentIndexFreq = (Integer) resultArr[j][1];
					currentIndexFreq += freq;
					resultArr[j][1] = currentIndexFreq;
					found = true;
				}
			}
			if (!found) {
				resultArr[entryCount][0] = fileName;
				resultArr[entryCount][1] = freq;
				entryCount++;
			}
			if (head3.getLink() != null) {
				head3 = head3.getLink();
			} else {
				break;
			}

		}

		return resultArr;
	}

	public int locate(int index, K key) {
		boolean found = false;
		while (!found && hashTable[index] != null) {
			if (hashTable[index].isIn() && key.equals(hashTable[index].getKey())) {
				found = true;
			} else {
				index = (index + 1) % hashTable.length;
			}

		}
		int result = -1;
		if (found) {
			result = index;
		}
		return result;
	}

	public V getValue(K key) {
		V result = null;
		int index = getHashIndex(key);
		index = locate(index, key);
		if (index != -1)
			result = hashTable[index].getValue();
		return result;
	}

	public boolean contains(K key) {
		int index = getHashIndex(key);
		index = locate(index, key);
		if (index != -1) {
			return true;
		}
		return false;
	}

	public void setHashType(int inputHashType) {
		hashType = inputHashType;
	}

	public void setCollType(int inputCollType) {
		collType = inputCollType;
	}

	public int getHashType() {
		return hashType;
	}

	public int getCollType() {
		return collType;
	}

	public K getKey(int idx) {
		return hashTable[idx].getKey();
	}

	public int getCollisions() {
		return coll;
	}

	public int getLength() {
		return hashTable.length;
	}

	public boolean isEmpty() {
		return numberOfEntries == 0;
	}

	public double getSize() {
		return numberOfEntries;
	}

	public double getLocUsed() {
		return locationsUsed;
	}

	public double getLoadFactor() {
		return load_factor;
	}

	public int locateIndex(K key) {
		return locate(getHashIndex(key), key);
	}

	public void clear() {
		while (getKeyIterator().hasNext()) {
			remove(getKeyIterator().next());
		}
	}

	public Iterator<V> getValueIterator() {
		return new ValueIterator();
	}

	public Iterator<K> getKeyIterator() {
		return new KeyIterator();
	}

	/////////////////////////////////
	private class TableEntry<T, S> {
		private T value;
		private S key;
		private SingleLinkedList freqSLL;
		private boolean inTable;
		private Object fileName;
		private int freqCounter = 0;

		private TableEntry(T value, S key) {
			this.value = value;
			this.key = key;
			inTable = true;
		}

		private TableEntry(T value, S key, SingleLinkedList inputSLL) {
			this.value = value;
			this.key = key;
			this.freqSLL = inputSLL;
			inTable = true;
		}

		public int getFreq() {
			return freqCounter;
		}

		public void setFreq(int inputFreq) {
			this.freqCounter = inputFreq;
		}

		private SingleLinkedList getSLL() {
			return freqSLL;
		}

		private S getKey() {
			return key;
		}

		private T getValue() {
			return value;
		}

		private void setKey(S key) {
			this.key = key;
		}

		private boolean isRemoved() {
			return inTable == false;
		}

		private void setToRemoved() {
			inTable = false;
		}

		private boolean isIn() {
			return inTable == true;
		}
	}

	////////////////////////////////
	private class KeyIterator implements Iterator<K> {
		private int currentIndex;
		private double numberLeft;

		private KeyIterator() {
			currentIndex = 0;
			numberLeft = numberOfEntries;
		}

		public boolean hasNext() {
			return numberLeft > 0;
		}

		public K next() {
			K result = null;
			if (hasNext()) {
				while ((hashTable[currentIndex] == null) || hashTable[currentIndex].isRemoved()) {
					currentIndex++;
				}
				result = hashTable[currentIndex].getKey();
				numberLeft--;
				currentIndex++;
			} else
				throw new NoSuchElementException();
			return result;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	private class ValueIterator implements Iterator<V> {
		private int currentIndex;
		private double numberLeft;

		private ValueIterator() {
			currentIndex = 0;
			numberLeft = numberOfEntries;
		}

		public boolean hasNext() {
			return numberLeft > 0;
		}

		public V next() {
			V result = null;
			if (hasNext()) {
				while ((hashTable[currentIndex] == null) || hashTable[currentIndex].isRemoved()) {
					currentIndex++;
				}
				result = hashTable[currentIndex].getValue();
				numberLeft--;
				currentIndex++;
			} else
				throw new NoSuchElementException();
			return result;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

}
