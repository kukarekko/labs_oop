package org.example.factory;

import org.example.model.PartType;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class FactoryUI extends JFrame {

    //склады
    private JLabel bodyCountLabel = new JLabel("0", SwingConstants.CENTER);
    private JLabel engineCountLabel = new JLabel("0", SwingConstants.CENTER);
    private JLabel accessoryCountLabel = new JLabel("0", SwingConstants.CENTER);

    //готовые машины
    private JLabel carCountLabel = new JLabel("0", SwingConstants.CENTER);
    private JLabel totalCarsLabel = new JLabel("0", SwingConstants.CENTER);

    //очередь задач
    private JLabel queueSizeLabel = new JLabel("0", SwingConstants.CENTER);

    // поставщики
    private JLabel bodySuppliersLabel = new JLabel("0", SwingConstants.CENTER);
    private JLabel engineSuppliersLabel = new JLabel("0", SwingConstants.CENTER);
    private JLabel accessorySuppliersLabel = new JLabel("0", SwingConstants.CENTER);

    //сборщики и дилеры
    private JLabel workersLabel = new JLabel("0", SwingConstants.CENTER);
    private JLabel dealersLabel = new JLabel("0", SwingConstants.CENTER);

    private JSlider bodySupplierSpeedSlider = new JSlider(JSlider.HORIZONTAL, 100, 3000, 1000);
    private JSlider engineSupplierSpeedSlider = new JSlider(JSlider.HORIZONTAL, 100, 3000, 1000);
    private JSlider accessorySupplierSpeedSlider = new JSlider(JSlider.HORIZONTAL, 100, 3000, 1000);
    private JSlider workerSpeedSlider = new JSlider(JSlider.HORIZONTAL, 100, 3000, 500);
    private JSlider dealerSpeedSlider = new JSlider(JSlider.HORIZONTAL, 500, 5000, 2000);

    private JSpinner bodySupplierCountSpinner = new JSpinner(new SpinnerNumberModel(1, 0, 5, 1));
    private JSpinner engineSupplierCountSpinner = new JSpinner(new SpinnerNumberModel(1, 0, 5, 1));
    private JSpinner accessorySupplierCountSpinner = new JSpinner(new SpinnerNumberModel(1, 0, 5, 1));
    private JSpinner workerCountSpinner;
    private JSpinner dealerCountSpinner;

    private JButton startButton = new JButton("Старт");
    private JButton stopButton = new JButton("Стоп");
    private JButton resetButton = new JButton("Сброс");

    private volatile boolean running = false;

    public FactoryUI() {
        setTitle("Эмулятор работы фабрики по производству автомобилей");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 800);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(240, 240, 245));

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(240, 240, 245));

        JPanel topPanel = createStoragePanel();
        JPanel bottomPanel = createControlPanel();

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    //панель со складами
    private JPanel createStoragePanel() {
        JPanel panel = new JPanel(new GridLayout(1, 6, 15, 15));  // ← 6 карточек
        panel.setBackground(new Color(240, 240, 245));

        panel.add(createStatCard("кузова", bodyCountLabel, new Color(66, 133, 244)));
        panel.add(createStatCard("двигатели", engineCountLabel, new Color(234, 67, 53)));
        panel.add(createStatCard("аксессуары", accessoryCountLabel, new Color(52, 168, 83)));
        panel.add(createStatCard("готовые машины", carCountLabel, new Color(251, 188, 4)));
        panel.add(createStatCard("всего произведенно", totalCarsLabel, new Color(128, 0, 128)));
        panel.add(createStatCard("очередь задач", queueSizeLabel, new Color(255, 140, 0)));  // ← НОВАЯ КАРТОЧКА

        return panel;
    }

    //создание одного квадратика с числом
    private JPanel createStatCard(String title, JLabel valueLabel, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder( //рамка
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(15, 10, 15, 10)
        ));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(color);

        valueLabel.setFont(new Font("Arial", Font.BOLD, 26));
        valueLabel.setForeground(color);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    //панель управления
    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));  // ← меняем на BorderLayout
        panel.setBackground(new Color(240, 240, 245));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "настройка и управление",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14)
        ));

        //панель скоростей (левая половина)
        JPanel speedPanel = new JPanel(new GridLayout(5, 1, 10, 15));
        speedPanel.setBackground(new Color(240, 240, 245));
        speedPanel.setBorder(BorderFactory.createTitledBorder("скорость (мс)"));

        JLabel bodySupplierSpeedLabel = new JLabel(bodySupplierSpeedSlider.getValue() + " мс");
        JLabel engineSupplierSpeedLabel = new JLabel(engineSupplierSpeedSlider.getValue() + " мс");
        JLabel accessorySupplierSpeedLabel = new JLabel(accessorySupplierSpeedSlider.getValue() + " мс");
        JLabel workerSpeedLabel = new JLabel(workerSpeedSlider.getValue() + " мс");
        JLabel dealerSpeedLabel = new JLabel(dealerSpeedSlider.getValue() + " мс");

        speedPanel.add(createSpeedControl("поставщики кузовов", bodySupplierSpeedSlider, bodySupplierSpeedLabel));
        speedPanel.add(createSpeedControl("поставщики двигателей", engineSupplierSpeedSlider, engineSupplierSpeedLabel));
        speedPanel.add(createSpeedControl("поставщики аксессуаров", accessorySupplierSpeedSlider, accessorySupplierSpeedLabel));
        speedPanel.add(createSpeedControl("сборщики", workerSpeedSlider, workerSpeedLabel));
        speedPanel.add(createSpeedControl("дилеры", dealerSpeedSlider, dealerSpeedLabel));

        //панель количества (правая половина)
        JPanel countPanel = new JPanel(new GridLayout(5, 1, 10, 15));
        countPanel.setBackground(new Color(240, 240, 245));
        countPanel.setBorder(BorderFactory.createTitledBorder("количество"));

        JPanel bodySupplierCountPanel = createCountControl("поставщики кузовов", 1, 0, 5);
        bodySupplierCountSpinner = (JSpinner) ((JPanel) bodySupplierCountPanel.getComponent(1)).getComponent(0);

        JPanel engineSupplierCountPanel = createCountControl("поставщики двигателей", 1, 0, 5);
        engineSupplierCountSpinner = (JSpinner) ((JPanel) engineSupplierCountPanel.getComponent(1)).getComponent(0);

        JPanel accessorySupplierCountPanel = createCountControl("поставщики аксессуаров", 1, 0, 5);
        accessorySupplierCountSpinner = (JSpinner) ((JPanel) accessorySupplierCountPanel.getComponent(1)).getComponent(0);

        JPanel workerCountPanel = createCountControl("сборщиков", 2, 1, 5);
        workerCountSpinner = (JSpinner) ((JPanel) workerCountPanel.getComponent(1)).getComponent(0);

        JPanel dealerCountPanel = createCountControl("дилеров", 2, 1, 5);
        dealerCountSpinner = (JSpinner) ((JPanel) dealerCountPanel.getComponent(1)).getComponent(0);

        countPanel.add(bodySupplierCountPanel);
        countPanel.add(engineSupplierCountPanel);
        countPanel.add(accessorySupplierCountPanel);
        countPanel.add(workerCountPanel);
        countPanel.add(dealerCountPanel);

        //объединяем лево и право
        JPanel settingsPanel = new JPanel(new GridLayout(1, 2, 15, 15));
        settingsPanel.setBackground(new Color(240, 240, 245));
        settingsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        settingsPanel.add(speedPanel);
        settingsPanel.add(countPanel);

        //панель кнопок
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(240, 240, 245));

        startButton.setFont(new Font("Arial", Font.BOLD, 16));
        startButton.setBackground(new Color(52, 168, 83));
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.setPreferredSize(new Dimension(120, 40));

        stopButton.setFont(new Font("Arial", Font.BOLD, 16));
        stopButton.setBackground(new Color(234, 67, 53));
        stopButton.setForeground(Color.WHITE);
        stopButton.setFocusPainted(false);
        stopButton.setPreferredSize(new Dimension(120, 40));
        stopButton.setEnabled(false);

        resetButton.setFont(new Font("Arial", Font.BOLD, 16));
        resetButton.setBackground(new Color(100, 100, 100));
        resetButton.setForeground(Color.WHITE);
        resetButton.setFocusPainted(false);
        resetButton.setPreferredSize(new Dimension(120, 40));

        startButton.addActionListener(e -> startFactory());
        stopButton.addActionListener(e -> stopFactory());
        resetButton.addActionListener(e -> resetFactory());

        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        buttonPanel.add(resetButton);

        //собираем всё в основной панели
        panel.add(settingsPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    //панель с ползунком
    private JPanel createSpeedControl(String label, JSlider slider, JLabel valueLabel) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel titleLabel = new JLabel(label, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 12));

        slider.setMajorTickSpacing(1000); //крупные деления
        slider.setMinorTickSpacing(500); //мелкие деления
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setFont(new Font("Arial", Font.PLAIN, 10));

        valueLabel.setFont(new Font("Arial", Font.BOLD, 12));
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);

        slider.addChangeListener(e -> {
            int val = slider.getValue();
            valueLabel.setText(val + " мс");
        });

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(slider, BorderLayout.CENTER);
        panel.add(valueLabel, BorderLayout.SOUTH);

        return panel;
    }

    //панель с количеством
    private JPanel createCountControl(String label, int defaultValue, int min, int max) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel titleLabel = new JLabel(label, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 12));

        JSpinner spinner = new JSpinner(new SpinnerNumberModel(defaultValue, min, max, 1));
        spinner.setFont(new Font("Arial", Font.BOLD, 14));
        ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField().setHorizontalAlignment(JTextField.CENTER);

        JPanel spinnerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        spinnerPanel.setBackground(Color.WHITE);
        spinnerPanel.add(spinner);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(spinnerPanel, BorderLayout.CENTER);

        return panel;
    }


    public int getWorkerSpeed() { return workerSpeedSlider.getValue(); }
    public int getDealerSpeed() { return dealerSpeedSlider.getValue(); }
    public int getBodySupplierCount() { return (int) bodySupplierCountSpinner.getValue(); }
    public int getEngineSupplierCount() { return (int) engineSupplierCountSpinner.getValue(); }
    public int getAccessorySupplierCount() { return (int) accessorySupplierCountSpinner.getValue(); }
    public int getWorkerCount() { return (int) workerCountSpinner.getValue(); }
    public int getDealerCount() { return (int) dealerCountSpinner.getValue(); }
    public boolean isRunning() { return running; }
    public int getBodySupplierSpeed() { return bodySupplierSpeedSlider.getValue(); }
    public int getEngineSupplierSpeed() { return engineSupplierSpeedSlider.getValue(); }
    public int getAccessorySupplierSpeed() { return accessorySupplierSpeedSlider.getValue(); }

    public void updateBody(int count) {
        SwingUtilities.invokeLater(() -> bodyCountLabel.setText(String.valueOf(count)));
    }

    public void updateEngine(int count) {
        SwingUtilities.invokeLater(() -> engineCountLabel.setText(String.valueOf(count)));
    }

    public void updateAccessory(int count) {
        SwingUtilities.invokeLater(() -> accessoryCountLabel.setText(String.valueOf(count)));
    }

    public void updateCar(int count) {
        SwingUtilities.invokeLater(() -> carCountLabel.setText(String.valueOf(count)));
    }

    public void updateTotal(int total) {
        SwingUtilities.invokeLater(() -> totalCarsLabel.setText(String.valueOf(total)));
    }

    public void updateQueueSize(int size) {
        SwingUtilities.invokeLater(() -> queueSizeLabel.setText(String.valueOf(size)));
    }

    public void updateBodySuppliers(int count) {
        SwingUtilities.invokeLater(() -> bodySuppliersLabel.setText(String.valueOf(count)));
    }

    public void updateEngineSuppliers(int count) {
        SwingUtilities.invokeLater(() -> engineSuppliersLabel.setText(String.valueOf(count)));
    }

    public void updateAccessorySuppliers(int count) {
        SwingUtilities.invokeLater(() -> accessorySuppliersLabel.setText(String.valueOf(count)));
    }

    public void updateWorkers(int count) {
        SwingUtilities.invokeLater(() -> workersLabel.setText(String.valueOf(count)));
    }

    public void updateDealers(int count) {
        SwingUtilities.invokeLater(() -> dealersLabel.setText(String.valueOf(count)));
    }

    private void startFactory() {
        running = true;
        startButton.setEnabled(false);
        stopButton.setEnabled(true);

        updateBodySuppliers(getBodySupplierCount());
        updateEngineSuppliers(getEngineSupplierCount());
        updateAccessorySuppliers(getAccessorySupplierCount());
        updateWorkers(getWorkerCount());
        updateDealers(getDealerCount());

        new Thread(() -> {
            try {
                FactoryController.startFactory(this);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    private void stopFactory() {
        running = false;
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        FactoryController.stopFactory();
        System.out.println("Фабрика остановлена");
    }

    private void resetFactory() {
        stopFactory();
        FactoryController.resetStorages();
        updateBody(0);
        updateEngine(0);
        updateAccessory(0);
        updateCar(0);
        updateTotal(0);
        updateQueueSize(0);
        System.out.println("Фабрика сброшена");
    }

}