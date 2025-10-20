# 🚀 Virtual Plant Growth Simulator

A meaningful Java application that teaches life lessons through plant growth simulation.

## � About This Project

This simulator demonstrates the delicate balance of growth and excess through interactive plant care. Each stage teaches profound lessons about personal development, while showing the consequences of overindulgence.

## � Prerequisites

- **Java 11 or higher** (JDK 17 recommended)
- Any operating system (Windows/Mac/Linux)

## 🛠️ How to Compile and Run

### Step 1: Compile the Source Code
```bash
# Navigate to the project directory
cd VirtualPlantGrowthSimulator

# Compile all Java files
javac -d out src/com/plantgrowth/*.java
```

### Step 2: Run the GUI Version
```bash
# Run the graphical interface
java -cp out com.plantgrowth.VirtualPlantGrowthSimulator
```

### Step 3: Run the Console Version (Optional)
```bash
# Run the text-based interface
java -cp out com.plantgrowth.ConsolePlantSimulator
```

## 🎮 How to Use

### GUI Version Features:
- **Interactive watering** - Click "Water" button to nurture your plant
- **Visual growth stages** - Watch your plant progress through 7 meaningful stages
- **Life lessons** - Each stage displays profound messages about personal growth
- **Consequences of excess** - Overwatering leads to decline, teaching balance
- **Reset functionality** - Start fresh with new wisdom

### Complete Growth Journey:

| Stage | Name | Life Lesson |
|-------|------|-------------|
| 1 | Seed | Foundation: Plant strong roots before reaching for the sky |
| 2 | Sprout | Emergence: Break through limitations with consistent effort |
| 3 | Young Plant | Growth: Build strength through challenges and adaptation |
| 4 | Flowering Plant | Blossom: Share your beauty and wisdom with the world |
| 5 | Wilting | Warning: Excess drains your vital energy |
| 6 | Dying | Decline: Overindulgence leads to irreversible damage |
| 7 | Dead | Lesson: Balance is the key to sustainable growth |

## 💡 Gameplay Tips

- **Water wisely** - Each stage requires exactly 2 waterings to progress
- **Learn from decline** - Overwatering teaches about the dangers of excess
- **Reset and reflect** - Use reset to apply lessons learned
- **Balance is key** - The core message: moderation sustains growth

## 🏗️ Project Structure

```
VirtualPlantGrowthSimulator/
├── src/
│   └── com/plantgrowth/
│       ├── VirtualPlantGrowthSimulator.java  # Main GUI application
│       ├── PlantPanel.java                    # Custom plant rendering component
│       ├── ConsolePlantSimulator.java         # Text-based version
│       └── OverWateringException.java         # Custom exception class
└── HOW_TO_RUN.md                              # This file
```

## 🎯 Educational Value

This simulator serves as a metaphor for personal development:
- **Growth stages** teach positive life principles
- **Decline stages** demonstrate consequences of imbalance
- **Reset function** symbolizes new beginnings and learning from mistakes

Perfect for teaching programming concepts, UI design, and life lessons simultaneously!

---

**� Remember: Just as plants need balanced care to thrive, so do our ambitions and goals in life.**
