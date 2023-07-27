package dataAS1;

public class SingleLinkedList {
	Node head;
	int freqInput;
	int freq;

	public void add(Node dataToAdd) {
		if (head == null) {
			head = dataToAdd;
			head.setFreq(dataToAdd.getFreq());
		} else {
			Node temp = head;
			while (temp.getLink() != null)
				temp = temp.getLink();
			Node newnode = dataToAdd;
			newnode.setFileName(dataToAdd.getFileName());
			newnode.setFreq(dataToAdd.getFreq());
			temp.setLink(newnode);
		}
	}

	public void display() {
		if (head == null)
			System.out.println("Not found!");
		else {
			Node temp = head;
			while (temp != null) {
				if (temp.getFreq() == 1) {
					System.out.println("At " + temp.getFileName() + ", got " + temp.getFreq() + " frequency.");
				} else {
					System.out.println("At " + temp.getFileName() + ", got " + temp.getFreq() + " frequencies.");
				}

				temp = temp.getLink();
			}
			System.out.println();
		}
	}

	public int size() {
		int count = 0;
		if (head == null) {
			// System.out.println("linked list is empty");
		}

		else {
			Node temp = head;
			while (temp != null) {
				count++;
				temp = temp.getLink();
			}
		}
		return count;
	}

	public void searchDataAndFreq(Object dataToSearch, int freqDataToAdd) {
		if (head == null) {
			Node newnode = new Node(dataToSearch, freqDataToAdd);
			head = newnode;
		} else {

			if (head.getFileName().equals(dataToSearch)) {
				head.setFreq(freqDataToAdd);
			} else {

				Node temp = head;
				boolean found = false;
				while (temp != null) {

					if (temp.getFileName().equals(dataToSearch)) {
						int tempNum = temp.getFreq();
						tempNum = tempNum + 1;
						temp.setFreq(tempNum);
						found = true;
					} else {

					}
					if (temp.getLink() == null) {
						break;
					} else {
						temp = temp.getLink();
					}

				}
				if (!found) {
					Node newnode = new Node(dataToSearch, 1);
					temp.setLink(newnode);
				}
			}
		}

	}

}
